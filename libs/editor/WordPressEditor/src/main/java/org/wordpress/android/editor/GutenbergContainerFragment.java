package org.wordpress.android.editor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import org.wordpress.mobile.WPAndroidGlue.WPAndroidGlueCode;
import org.wordpress.mobile.WPAndroidGlue.WPAndroidGlueCode.OnGetContentTimeout;
import org.wordpress.mobile.WPAndroidGlue.WPAndroidGlueCode.OnMediaLibraryButtonListener;

public class GutenbergContainerFragment extends Fragment {
    public static final String TAG = "gutenberg_container_fragment_tag";

    private static final String ARG_IS_NEW_POST = "param_is_new_post";

    private boolean mHtmlModeEnabled;

    private WPAndroidGlueCode mWPAndroidGlueCode;

    public static GutenbergContainerFragment newInstance(boolean isNewPost) {
        GutenbergContainerFragment fragment = new GutenbergContainerFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_NEW_POST, isNewPost);
        fragment.setArguments(args);
        return fragment;
    }

    public void attachToContainer(ViewGroup viewGroup) {
        mWPAndroidGlueCode.attachToContainer(viewGroup);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isNewPost = getArguments() != null && getArguments().getBoolean(ARG_IS_NEW_POST);

        mWPAndroidGlueCode = new WPAndroidGlueCode();
        mWPAndroidGlueCode.onCreate(getContext());
        mWPAndroidGlueCode.onCreateView(
                getContext(),
                mHtmlModeEnabled,
                new OnMediaLibraryButtonListener() {
                    @Override public void onMediaLibraryButtonClick() {
                        ((GutenbergEditorFragment) getParentFragment()).onToolbarMediaButtonClicked();
                    }
                },
                getActivity().getApplication(),
                BuildConfig.DEBUG,
                BuildConfig.BUILD_GUTENBERG_FROM_SOURCE,
                isNewPost);
    }

    @Override
    public void onPause() {
        super.onPause();

        mWPAndroidGlueCode.onPause(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        mWPAndroidGlueCode.onResume(this, getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mWPAndroidGlueCode.onDestroy(getActivity());
    }

    public void setContent(String title, String postContent) {
        mWPAndroidGlueCode.setContent(title, postContent);
    }

    public void toggleHtmlMode() {
        mHtmlModeEnabled = !mHtmlModeEnabled;

        mWPAndroidGlueCode.toggleEditorMode();
    }

    /**
     * Returns the contents of the content field from the JavaScript editor. Should be called from a background thread
     * where possible.
     */
    public CharSequence getContent(CharSequence originalContent, OnGetContentTimeout onGetContentTimeout) {
        return mWPAndroidGlueCode.getContent(originalContent, onGetContentTimeout);
    }

    public CharSequence getTitle(OnGetContentTimeout onGetContentTimeout) {
        return mWPAndroidGlueCode.getTitle(onGetContentTimeout);
    }

    public void appendMediaFile(final String mediaUrl) {
        mWPAndroidGlueCode.appendMediaFile(mediaUrl);
    }

    public void showDevOptionsDialog() {
        mWPAndroidGlueCode.showDevOptionsDialog();
    }
}
