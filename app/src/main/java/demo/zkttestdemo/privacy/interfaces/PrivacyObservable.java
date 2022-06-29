package demo.zkttestdemo.privacy.interfaces;

public interface PrivacyObservable {
    public void addObserver(PrivacyObserver observer);
    public void removeObserver(PrivacyObserver observer);
    public void notifiObservers();
}