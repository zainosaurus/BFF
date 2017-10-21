package com.example.bff;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private static final int IMG_REQUEST = 1;
    private int buttonState;
    private SharedPreferences pref;
    private TextView label;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        label = (TextView) findViewById(R.id.label);

        pref = this.getPreferences(Context.MODE_PRIVATE);
        buttonState = pref.getInt("last_state", 0);
        init(button, label, buttonState);
    }


    private void init(Button butt, TextView txt, int state) {

        if (state == 0) {
            butt.setText("Button");
            txt.setText("Don't Press The Button");
        }
        else if (state == 1) {
            butt.setText("I'm Sorry!");
            txt.setText("They pushed the button.\nThey FUCKING pushed it.\nI can't believe you've done this.");
        }
        else if (state == 2){
            butt.setText("Fine");
            txt.setText("Fine. Just don't do it again");
        }
    }

    /**
     * Called when Button is Pressed
     */
    public void buttonPressed(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");

        if (buttonState == 0) {
            buttonState++;
            init(button, label, buttonState);
        }
        else if (buttonState == 1) {
            buttonState++;
            init(button, label, buttonState);
        }
        else if (buttonState == 2) {
            buttonState = 0;
            init(button, label, buttonState);
        }

    }

    /**
     * Called when Image is picked
     */
    public void pickImage(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, IMG_REQUEST);
    }


    /**
     * Called when camera is requested
     */
    public void cameraSensorClick(View view) {

    }


    /**
     * Activity Result
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            ImageView imageView = (ImageView) findViewById(R.id.image);
            imageView.setImageURI(selectedImage);
            label.setText(selectedImage.toString());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("last_state", buttonState);
        edit.apply();
    }

}
