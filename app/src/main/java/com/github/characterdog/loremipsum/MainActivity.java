package com.github.characterdog.loremipsum;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import es.dmoral.toasty.Toasty;

import java.util.StringTokenizer;

import static com.github.characterdog.loremipsum.AboutActivity.EXTRA_PRIVACY_POLICY;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    private final static int NUMBER_WORDS_IN_LOREM_RES = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBar seekBar = findViewById(R.id.seekBar);
        final TextView sizeTextView = findViewById(R.id.textView_size);
        final TextView loremTextView = findViewById(R.id.text);
        Button shareButton = findViewById(R.id.button_share);
        Button copyButton = findViewById(R.id.button_clipboard);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateUI(sizeTextView, loremTextView, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        updateUI(sizeTextView, loremTextView, seekBar.getProgress());

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, loremTextView.getText());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Lorem ipsum", loremTextView.getText());
                    clipboard.setPrimaryClip(clip);
                    Toasty.success(getApplicationContext(), getString(R.string.copy_success)).show();
                } catch (Exception e) {
                    Log.e(TAG, "Error copying to clipboard", e);
                    Toasty.error(getApplicationContext(), getString(R.string.copy_error)).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.action_privacy) {
            Intent i = new Intent(this, AboutActivity.class);
            i.putExtra(EXTRA_PRIVACY_POLICY, true);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void updateUI(TextView sizeTextView, TextView loremIpsumTextView, int progress) {
        int wordCount = getWordCount(progress);
        sizeTextView.setText(getString(R.string.words, wordCount));
        loremIpsumTextView.setText(getLorem(wordCount));
    }

    String getLorem(int size) {
        int timesFullLorem = size / NUMBER_WORDS_IN_LOREM_RES;
        StringBuilder lorem = new StringBuilder();
        for (int i = 0; i < timesFullLorem; i++) {
            lorem.append(getString(R.string.lorem)).append(" ");
        }
        StringTokenizer stringTokenizer = new StringTokenizer(getString(R.string.lorem));
        for(int i = 0; i < size % NUMBER_WORDS_IN_LOREM_RES; i++) {
            lorem.append(stringTokenizer.nextToken()).append(" ");
        }
        lorem.deleteCharAt(lorem.length() - 1).append(".");
        return lorem.toString();
    }

    int getWordCount(int position) {
        switch (position) {
            case 0: return 10;
            case 1: return 20;
            case 2: return 50;
            case 3: return 100;
            case 4: return 200;
            case 5: return 500;
            case 6: return 1000;
            case 7: return 2000;
            case 8: return 5000;
            default: return 10000;
        }
    }
}
