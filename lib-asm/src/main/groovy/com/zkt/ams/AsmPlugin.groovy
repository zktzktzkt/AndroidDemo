package com.zkt.ams

import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import me.zkt.asm.AsmClassVisitor
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES

class AsmPlugin extends Transform implements Plugin<Project> {

    public static File tempFile
    public static List<String> thirdPackageList

    @Override
    void apply(Project project) {
        initDir(project)
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(this)
//        android.registerTransform(new AutoTrackTransform())

        //创建extension
        MyExtension extension = project.extensions.create('myExtension', MyExtension)

        // 当前Project配置完成后，会回调project.afterEvaluate()
        project.afterEvaluate(new Action<Project>() {
            @Override
            void execute(Project p) {
                thirdPackageList = extension.thirdPackage
                println "集合：" + thirdPackageList.toString()
            }
        })
    }

    @Override
    String getName() {
        return "AsmPlugin"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    static void initDir(Project project) {
        if (!project.buildDir.exists()) {
            project.buildDir.mkdirs()
        }
        tempFile = new File(project.buildDir, "asm")
        if (!tempFile.exists()) {
            tempFile.mkdir()
        }
    }

    @Override
    void transform(TransformInvocation transformInvocation) {
        println '--------------- AsmPlugin visit start --------------- '
        def startTime = System.currentTimeMillis()
        Collection<TransformInput> inputs = transformInvocation.inputs
        TransformOutputProvider outputProvider = transformInvocation.outputProvider
        //删除之前的输出
        if (outputProvider != null)
            outputProvider.deleteAll()
        //遍历inputs
        inputs.each { TransformInput input ->
            //遍历directoryInputs
            input.directoryInputs.each { DirectoryInput directoryInput ->
                handleDirectoryInput(directoryInput, outputProvider)
            }

            //遍历jarInputs
            input.jarInputs.each { JarInput jarInput ->
                handleJarInputs(jarInput, outputProvider)
            }
        }
        def cost = (System.currentTimeMillis() - startTime) / 1000
        println '--------------- AsmPlugin visit end --------------- '
        println "AsmPlugin cost : $cost s"
    }

    /**
     * 处理文件目录下的class文件
     */
    static void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        //是否是目录
        if (directoryInput.file.isDirectory()) {
            //列出目录所有文件（包含子文件夹，子文件夹内文件）
            directoryInput.file.eachFileRecurse { File file ->
                def name = file.name
                if (checkClassFile(name)) {
                    println '----------- deal with "class" file <' + name + '> -----------'
                    ClassReader classReader = new ClassReader(file.bytes)
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    ClassVisitor cv = new AsmClassVisitor(classWriter)
                    classReader.accept(cv, EXPAND_FRAMES)
                    byte[] code = classWriter.toByteArray()
                    FileOutputStream fos = new FileOutputStream(
                            file.parentFile.absolutePath + File.separator + name)
                    fos.write(code)
                    fos.close()
                    saveModifiedClassForCheck(file)
                }
            }
        }
        //处理完输入文件之后，要把输出给下一个任务
        def dest = outputProvider.getContentLocation(directoryInput.name,
                directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
        FileUtils.copyDirectoryToDirectory(directoryInput.file, dest)
    }

    /**
     * 处理Jar中的class文件
     */
    static void handleJarInputs(JarInput jarInput, TransformOutputProvider outputProvider) {
        if (jarInput.file.getAbsolutePath().endsWith(".jar")) {
            //重名名输出文件,因为可能同名,会覆盖
            def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
            def jarName = jarInput.name
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length() - 4)
            }

            File tmpFile = new File(jarInput.file.getParent() + File.separator + "classes_temp.jar")
            // println "tmpFile path->"+tmpFile.getAbsolutePath()
            //避免上次的缓存被重复插入
            if (tmpFile.exists()) {
                tmpFile.delete()
            }

            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpFile))
            JarFile jarFile = new JarFile(jarInput.file)
            Enumeration enumeration = jarFile.entries()

            //遍历jar中的每一个元素（遍历类）
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()
                ZipEntry zipEntry = new ZipEntry(entryName)
                InputStream inputStream = jarFile.getInputStream(jarEntry)

                //根据外部的声明决定是否处理此jar
                boolean needHandle = false
                for (int i = 0; i < thirdPackageList.size(); i++) {
                    if (entryName.startsWith(thirdPackageList.get(i))) {
                        needHandle = true
                        break
                    }
                }
                //这个class不需要处理，接着遍历
                if (!needHandle) {
                    continue
                }

                //检查class是否符合处理的条件
                if (checkClassFile(entryName)) {
                    //class文件处理
                    println '----------- deal with "jar" class file <' + entryName + '> -----------'
                    jarOutputStream.putNextEntry(zipEntry)
                    ClassReader classReader = new ClassReader(IOUtils.toByteArray(inputStream))
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    AsmClassVisitor cv = new AsmClassVisitor(classWriter)
                    classReader.accept(cv, EXPAND_FRAMES)
                    byte[] code = classWriter.toByteArray()
                    jarOutputStream.write(code)
                } else {
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }

                inputStream.close()
                jarOutputStream.closeEntry()
            }

            //结束
            jarOutputStream.close()
            jarFile.close()

            def dest = outputProvider.getContentLocation(jarName + md5Name,
                    jarInput.contentTypes, jarInput.scopes, Format.JAR)
            FileUtils.copyFile(tmpFile, dest)

            tmpFile.delete()
        }
    }

    /*
    保存插桩后的文件到临时目录 方便查看是否插桩正确
     */

    static void saveModifiedClassForCheck(File tempClass) {
        File dir = tempFile
        File checkJarFile = new File(dir, tempClass.getName().replace("/", "_"))
        if (checkJarFile.exists()) {
            checkJarFile.delete()
        }
        FileUtils.copyFile(tempClass, checkJarFile)
    }

    /**
     * 检查class文件是否需要处理
     * @param fileName
     * @return
     */
    static boolean checkClassFile(String name) {
        return (name.endsWith(".class")
                && !"R.class".equals(name)
                && !"BuildConfig.class".equals(name)
                && !name.startsWith("R\$")
                && !name.startsWith("META-INF/")
                && !name.startsWith("android/")
                && !name.startsWith("androidx/")
                && !name.startsWith("kotlin/")
                && !name.startsWith("kotlinx/")
                && !name.startsWith("org/intellij/")
                && !name.startsWith("org/jetbrains/")
                && !name.startsWith("com/google/")
        )
    }

}