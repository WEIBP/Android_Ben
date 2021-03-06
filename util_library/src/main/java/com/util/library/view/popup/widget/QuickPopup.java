package com.util.library.view.popup.widget;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

import com.util.library.view.popup.basepopup.BaseLazyPopupWindow;
import com.util.library.view.popup.basepopup.BasePopupFlag;
import com.util.library.view.popup.basepopup.QuickPopupConfig;

/**
 * Created by 大灯泡 on 2018/8/23.
 * <p>
 * 快速popup
 */
public class QuickPopup extends BaseLazyPopupWindow {

    private QuickPopupConfig mConfig;

    public QuickPopup(Fragment fragment, int width, int height, QuickPopupConfig config) {
        super(fragment, width, height);
        mConfig = config;
        if (mConfig == null) {
            throw new NullPointerException("QuickPopupConfig must be not null!");
        }
    }

    public QuickPopup(Dialog dialog, int width, int height, QuickPopupConfig config) {
        super(dialog, width, height);
        mConfig = config;
        if (mConfig == null) {
            throw new NullPointerException("QuickPopupConfig must be not null!");
        }
    }

    public QuickPopup(Context context, int width, int height, QuickPopupConfig config) {
        super(context, width, height);
        mConfig = config;
        if (mConfig == null) {
            throw new NullPointerException("QuickPopupConfig must be not null!");
        }
    }


    @Override
    public void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        applyConfigSetting(mConfig);
    }

    protected <C extends QuickPopupConfig> void applyConfigSetting(C config) {
        if (config.getPopupBlurOption() != null) {
            setBlurOption(config.getPopupBlurOption());
        } else {
            setBlurBackgroundEnable((config.flag & BasePopupFlag.BLUR_BACKGROUND) != 0, config.getOnBlurOptionInitListener());
        }

        setPopupFadeEnable((config.flag & BasePopupFlag.FADE_ENABLE) != 0);

        applyClick();

        setOffsetX(config.getOffsetX());
        setOffsetY(config.getOffsetY());

        setClipChildren((config.flag & BasePopupFlag.CLIP_CHILDREN) != 0);

        setOutSideDismiss((config.flag & BasePopupFlag.OUT_SIDE_DISMISS) != 0);
        setOutSideTouchable((config.flag & BasePopupFlag.OUT_SIDE_TOUCHABLE) != 0);
        setBackPressEnable((config.flag & BasePopupFlag.BACKPRESS_ENABLE) != 0);
        setPopupGravity(config.getGravity());
        setAlignBackground((config.flag & BasePopupFlag.ALIGN_BACKGROUND) != 0);
        setAlignBackgroundGravity(config.getAlignBackgroundGravity());
        setAutoLocatePopup((config.flag & BasePopupFlag.AUTO_LOCATED) != 0);
        setOverlayStatusbar((config.flag & BasePopupFlag.OVERLAY_STATUS_BAR) != 0);
        setOnDismissListener(config.getDismissListener());
        setBackground(config.getBackground());
        linkTo(config.getLinkedView());
        setMinWidth(config.getMinWidth());
        setMaxWidth(config.getMaxWidth());
        setMinHeight(config.getMinHeight());
        setMaxHeight(config.getMaxHeight());
        setOnKeyboardChangeListener(config.getOnKeyboardChangeListener());
        setKeyEventListener(config.getKeyEventListener());
    }

    private void applyClick() {
        HashMap<Integer, Pair<View.OnClickListener, Boolean>> eventsMap = mConfig.getListenersHolderMap();
        if (eventsMap == null || eventsMap.isEmpty()) return;
        for (Map.Entry<Integer, Pair<View.OnClickListener, Boolean>> entry : eventsMap.entrySet()) {
            int viewId = entry.getKey();
            final Pair<View.OnClickListener, Boolean> event = entry.getValue();
            View v = findViewById(viewId);
            if (v != null) {
                if (event.second) {
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (event.first != null) {
                                if (event.first instanceof OnQuickPopupClickListenerWrapper) {
                                    ((OnQuickPopupClickListenerWrapper) event.first).mQuickPopup = QuickPopup.this;
                                }
                                event.first.onClick(v);
                            }
                            dismiss();
                        }
                    });
                } else {
                    v.setOnClickListener(event.first);
                }
            }
        }
    }

    @Nullable
    public QuickPopupConfig getConfig() {
        return mConfig;
    }

    @Override
    protected Animation onCreateShowAnimation() {
        if (isConfigDestroyed()) return null;
        return mConfig.getShowAnimation();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        if (isConfigDestroyed()) return null;
        return mConfig.getDismissAnimation();
    }

    @Override
    protected Animator onCreateDismissAnimator() {
        if (isConfigDestroyed()) return null;
        return mConfig.getDismissAnimator();
    }

    @Override
    protected Animator onCreateShowAnimator() {
        if (isConfigDestroyed()) return null;
        return mConfig.getShowAnimator();
    }

    @Override
    public View onCreateContentView() {
        if (isConfigDestroyed()) return null;
        return createPopupById(mConfig.getContentViewLayoutid());
    }

    boolean isConfigDestroyed() {
        return mConfig == null || mConfig.isDestroyed();
    }

    @Override
    public void onDestroy() {
        if (mConfig != null) {
            mConfig.clear(true);
        }
        mConfig = null;
        super.onDestroy();
    }
}
