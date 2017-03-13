package com.arunsoorya.savethedate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.arunsoorya.savethedate.ocrlib.OcrCaptureActivity;
import com.google.android.gms.common.api.CommonStatusCodes;

public class ImageReaderPreviewActivity extends BaseActivity implements View.OnClickListener {
    private static final int RC_OCR_CAPTURE = 9003;
    private EditText textValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_reader_preview);

        textValue = (EditText) findViewById(R.id.text_value);

        findViewById(R.id.read_text).setOnClickListener(this);
        findViewById(R.id.submit).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_text) {
            // launch Ocr capture activity.
            Intent intent = new Intent(this, OcrCaptureActivity.class);
//            intent.putExtra(OcrCaptureActivity.AutoFocus, autoFocus.isChecked());
//            intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());

            startActivityForResult(intent, RC_OCR_CAPTURE);
        } else if (v.getId() == R.id.submit) {
            Intent intent = new Intent();
            intent.putExtra("data", textValue.getText());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    textValue.setText(text);
                } else {
                    showToast("No Text captured");
                }
            } else {
                showToast("No Text captured");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
