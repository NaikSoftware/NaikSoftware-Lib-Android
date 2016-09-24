package ua.naiksoftware.android.viewmodel;

/**
 * Simple interface for throwing progress and error callbacks from ViewModel
 */
public interface SimpleViewModelCallback {

    void showError(String error);
    void showMessage(String message);

    SimpleViewModelCallback EMPTY_CALLBACK = new SimpleViewModelCallback() {

        @Override
        public void showError(String error) {
        }

        @Override
        public void showMessage(String message) {
        }

        public void showProgress(String message) {
        }

        public void hideProgress() {
        }
    };
}
