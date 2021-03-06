package ua.kostenko.mydictionary.ui.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import ua.kostenko.mydictionary.App;
import ua.kostenko.mydictionary.R;
import ua.kostenko.mydictionary.core.local.database.dao.UnitDao;
import ua.kostenko.mydictionary.core.local.database.domain.SerializableMap;
import ua.kostenko.mydictionary.core.local.database.domain.Unit;
import ua.kostenko.mydictionary.core.webpart.enums.Languages;
import ua.kostenko.mydictionary.core.webpart.services.OnResultCallback;
import ua.kostenko.mydictionary.core.webpart.services.TranslateService;
import ua.kostenko.mydictionary.ui.iterfaces.OnUpdate;

import static ua.kostenko.mydictionary.core.commonutils.Utils.checkNotNull;
import static ua.kostenko.mydictionary.core.commonutils.Utils.isNotNull;

public class UnitCreateDialog {
    private final MaterialDialog.Builder builder;
    private final OnUpdate onUpdateAdapter;
    @NonNull private final MaterialDialog.SingleButtonCallback onNegativeButtonClick;
    @NonNull private final MaterialDialog.SingleButtonCallback onPositiveButtonClick;
    @Bind(R.id.source_text) EditText sourceEditText;
    @Bind(R.id.translation_text) TextView translationTextView;
    @Bind(R.id.translation_additional_text) TextView translationAdditionalTextView;
    @Bind(R.id.user_translation_text) EditText userTranslationEditText;
    @Bind(R.id.row_translation) LinearLayout rowTranslation;
    @Bind(R.id.row_additional) LinearLayout rowAdditional;
    @Bind(R.id.row_user_variant) TextInputLayout rowUserVariant;
    @BindString(R.string.dictionary_create_unit_dialog_translate) String positiveText;
    @BindString(R.string.standard_cancel) String negativeText;
    @Inject UnitDao unitDao;
    @Inject TranslateService<Unit> translateService;
    private boolean isTranslated;
    private Unit current;
    private MaterialDialog materialDialog;

    public UnitCreateDialog(@NonNull final Context context, @NonNull final LayoutInflater inflater,
                            @NonNull final OnUpdate onUpdate) {
        final View dialogView = inflater.inflate(R.layout.dialog_create_unit, null, false);
        ButterKnife.bind(this, dialogView);
        App.getAppComponent().inject(this);
        builder = new MaterialDialog.Builder(context);
        builder.customView(dialogView, true);
        onNegativeButtonClick = new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        };
        onPositiveButtonClick = new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (!isTranslated) {
                    translate(dialogView);
                } else {
                    save(dialog);
                    dialog.dismiss();
                }
            }
        };
        onUpdateAdapter = onUpdate;
        isTranslated = false;
    }

    public UnitCreateDialog(@NonNull final Context context, LayoutInflater inflater, Unit unit,
                            @NonNull final OnUpdate onUpdate) {
        this(context, inflater, onUpdate);
        sourceEditText.setText(unit.getSource());
        translationTextView.setText(unit.getTranslations());
        SerializableMap translationsAdditional = unit.getTranslationsAdditional();
        translationAdditionalTextView.setText(isNotNull(translationsAdditional) ? translationsAdditional.toString() : "");
        userTranslationEditText.setText(unit.getUserTranslation());
        current = unit;
    }

    private void save(MaterialDialog dialog) {
        unitDao.saveUnit(new Unit(getSourceText(), getTranslationText(),
                isNotNull(current) ? current.getTranslationsAdditional() : new SerializableMap(),
                getUserTranslationText(), current.getCounter(), current.getTechnologies()));
        if (isNotNull(onUpdateAdapter)) {
            onUpdateAdapter.update();
        }
        Toast.makeText(dialog.getView().getContext(), R.string.dialog_create_toast_message_ok, Toast.LENGTH_LONG).show();
    }

    private void translate(View v) {
        translateService.translate(Languages.ENGLISH, Languages.RUSSIAN, getSourceText(), new OnResultCallback<Unit>() {
            @Override
            public void onResult(Unit result) {
                isTranslated = true;
                current = result;
                translationTextView.setText(result.getTranslations());
                translationAdditionalTextView.setText(result.getTranslationsAdditional().toString());
                materialDialog.setActionButton(DialogAction.POSITIVE, R.string.dialog_create_add_unit);
                rowTranslation.setVisibility(View.VISIBLE);
                rowAdditional.setVisibility(View.VISIBLE);
                rowUserVariant.setVisibility(View.VISIBLE);
            }
        });
        Toast.makeText(v.getContext(), R.string.dialog_create_toast_message_request, Toast.LENGTH_LONG).show();
    }

    public void show() {
        getDialog().show();
    }

    private MaterialDialog getDialog() {
        checkNotNull(onNegativeButtonClick);
        checkNotNull(onPositiveButtonClick);
        builder.positiveText(positiveText);
        builder.autoDismiss(false);
        builder.negativeText(negativeText);
        builder.onPositive(onPositiveButtonClick);
        builder.onNegative(onNegativeButtonClick);
        materialDialog = builder.build();
        return materialDialog;
    }

    public String getSourceText() {
        return sourceEditText.getText().toString();
    }

    public String getTranslationText() {
        return translationTextView.getText().toString();
    }

    public String getUserTranslationText() {
        return userTranslationEditText.getText().toString();
    }
}
