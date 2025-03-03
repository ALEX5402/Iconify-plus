package com.drdisagree.iconify.ui.activities;

import static com.drdisagree.iconify.common.Const.SWITCH_ANIMATION_DELAY;
import static com.drdisagree.iconify.common.Const.SYSTEMUI_PACKAGE;
import static com.drdisagree.iconify.common.Preferences.NOTIF_TRANSPARENCY_SWITCH;
import static com.drdisagree.iconify.common.Preferences.QSALPHA_LEVEL;
import static com.drdisagree.iconify.common.Preferences.QSPANEL_BLUR_SWITCH;
import static com.drdisagree.iconify.common.Preferences.QS_TRANSPARENCY_SWITCH;
import static com.drdisagree.iconify.common.References.FABRICATED_QSPANEL_BLUR_RADIUS;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.drdisagree.iconify.R;
import com.drdisagree.iconify.config.Prefs;
import com.drdisagree.iconify.config.RPrefs;
import com.drdisagree.iconify.databinding.ActivityXposedTransparencyBlurBinding;
import com.drdisagree.iconify.ui.utils.ViewBindingHelpers;
import com.drdisagree.iconify.utils.FabricatedUtil;
import com.drdisagree.iconify.utils.SystemUtil;

public class XposedTransparencyBlur extends BaseActivity {

    private ActivityXposedTransparencyBlurBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityXposedTransparencyBlurBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Header
        ViewBindingHelpers.setHeader(this, binding.header.collapsingToolbar, binding.header.toolbar, R.string.activity_title_transparency_blur);

        // Qs Panel & Notification Shade Transparency
        binding.enableQsTransparency.setChecked(RPrefs.getBoolean(QS_TRANSPARENCY_SWITCH, false));
        binding.enableQsTransparency.setOnCheckedChangeListener(qsTransparencyListener);

        binding.enableNotifTransparency.setChecked(RPrefs.getBoolean(NOTIF_TRANSPARENCY_SWITCH, false));
        binding.enableNotifTransparency.setOnCheckedChangeListener(notifTransparencyListener);

        // Tansparency Alpha
        final int[] transparency = {RPrefs.getInt(QSALPHA_LEVEL, 60)};
        binding.transparencyOutput.setText(getResources().getString(R.string.opt_selected) + ' ' + transparency[0] + "%");
        binding.transparencySeekbar.setProgress(transparency[0]);
        binding.transparencySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                transparency[0] = progress;
                binding.transparencyOutput.setText(getResources().getString(R.string.opt_selected) + ' ' + progress + "%");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                RPrefs.putInt(QSALPHA_LEVEL, transparency[0]);
            }
        });

        // Qs Panel Blur
        Prefs.putBoolean(QSPANEL_BLUR_SWITCH, SystemUtil.isBlurEnabled());
        binding.enableBlur.setChecked(Prefs.getBoolean(QSPANEL_BLUR_SWITCH, false));
        binding.enableBlur.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Prefs.putBoolean(QSPANEL_BLUR_SWITCH, isChecked);
            if (isChecked) SystemUtil.enableBlur();
            else {
                SystemUtil.disableBlur();
                FabricatedUtil.disableOverlay(FABRICATED_QSPANEL_BLUR_RADIUS);
            }
        });

        final int[] blur_radius = {Prefs.getInt(FABRICATED_QSPANEL_BLUR_RADIUS, 23)};
        binding.blurOutput.setText(getResources().getString(R.string.opt_selected) + ' ' + blur_radius[0] + "px");
        binding.blurSeekbar.setProgress(blur_radius[0]);
        binding.blurSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blur_radius[0] = progress;
                binding.blurOutput.setText(getResources().getString(R.string.opt_selected) + ' ' + progress + "px");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Prefs.putInt(FABRICATED_QSPANEL_BLUR_RADIUS, blur_radius[0]);
                FabricatedUtil.buildAndEnableOverlay(SYSTEMUI_PACKAGE, FABRICATED_QSPANEL_BLUR_RADIUS, "dimen", "max_window_blur_radius", blur_radius[0] + "px");
                // Restart SystemUI
                new Handler(Looper.getMainLooper()).postDelayed(SystemUtil::restartSystemUI, SWITCH_ANIMATION_DELAY);
            }
        });
    }

    CompoundButton.OnCheckedChangeListener qsTransparencyListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            RPrefs.putBoolean(QS_TRANSPARENCY_SWITCH, isChecked);

            if (isChecked) {
                RPrefs.putBoolean(NOTIF_TRANSPARENCY_SWITCH, false);
                binding.enableNotifTransparency.setOnCheckedChangeListener(null);
                binding.enableNotifTransparency.setChecked(false);
                binding.enableNotifTransparency.setOnCheckedChangeListener(notifTransparencyListener);
            }

            new Handler(Looper.getMainLooper()).postDelayed(SystemUtil::restartSystemUI, SWITCH_ANIMATION_DELAY);
        }
    };

    CompoundButton.OnCheckedChangeListener notifTransparencyListener = (buttonView, isChecked) -> {
        RPrefs.putBoolean(NOTIF_TRANSPARENCY_SWITCH, isChecked);

        if (isChecked) {
            RPrefs.putBoolean(QS_TRANSPARENCY_SWITCH, false);
            binding.enableQsTransparency.setOnCheckedChangeListener(null);
            binding.enableQsTransparency.setChecked(false);
            binding.enableQsTransparency.setOnCheckedChangeListener(qsTransparencyListener);
        }

        new Handler(Looper.getMainLooper()).postDelayed(SystemUtil::restartSystemUI, SWITCH_ANIMATION_DELAY);
    };
}