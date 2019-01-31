package alex_bou.stucom.com.alex_bou.activities;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import alex_bou.stucom.com.alex_bou.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    final int GALLERY_IMAGE_REQUEST = 1;
    final int CAMERA_IMAGE_REQUEST = 2;

    TextInputEditText inputName;
    TextInputEditText inputEmail;
    File imgFile;
    CircleImageView imgProfile;
    SharedPreferences prefs;
    SharedPreferences.Editor editorPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set layout to display.
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get shared preferences.
        prefs = getPreferences(MODE_PRIVATE);

        // Get layout objects from layout.
        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        imgProfile = findViewById(R.id.imgProfile);

        // Set text inputs value if already saved in shared preferences, else set to empty string.
        // inputName.setText(prefs.getString("name",""));
        // inputEmail.setText(prefs.getString("email",""));

        // Get image file from internal files directory.
        // imgFile = new File(getFilesDir(), "imgProfile.jpg");

        // If image file exists assign its uri to profile image
        // if (imgFile.exists()) {
        //     imgProfile.setImageURI(Uri.fromFile(imgFile));
        // }

    }


    @Override
    protected void onPause() {
        super.onPause();
        // Save email and name to shared preferences.
        // This is done in this function because android fires it every time the application gets destroyed.
        // Get shared preferences editor.
        editorPrefs = prefs.edit();
        // Save email input current value.
        editorPrefs.putString("email", inputEmail.getText().toString());
        // Save name input current value.
        editorPrefs.putString("name", inputName.getText().toString());
        // Commit the changes to shared preferences.
        editorPrefs.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // This is fired when an activity returns some data after calling startActivityForResult.
        // First check if resultCode is OK and check request code specified in the intent.
        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK) {
            // Set layout image URI to new image selected from gallery.
            imgProfile.setImageURI(data.getData());
            // Delete last image saved in internal storage.
            imgFile.delete();
            try {
                // Try to fetch bitmap from media store. It was the easiest way to store it in internal memory,
                // this way I can still access the picture when deleted from the gallery.
                Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                // Save bitmap in internal storage see method below.
                saveImageFromBitmap(bitmapImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            // Get bitmap of the image taken in camera activity.
            Bitmap bitmapImage = (Bitmap) data.getExtras().get("data");
            // Delete last image in internal storage.
            imgFile.delete();
            // Set layout image bitmap to new bitmap generated by the camera.
            imgProfile.setImageBitmap(bitmapImage);
            // Save bitmap in internal storage see method below.
            saveImageFromBitmap(bitmapImage);
        }
    }

    /**
     * Saves image to application's internal storage.
     *
     * @param bitmapImage bitmap representing image.
     */
    private void saveImageFromBitmap(Bitmap bitmapImage) {
        FileOutputStream fos = null;
        try {
            // Create a new FileOutputStream for image file (this means a Stream for writing on the file)
            fos = new FileOutputStream(imgFile);
            // Use the compress method on the BitMap object to write image to the OutputStream.
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Always close Output stream reader.
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Shows dialog asking to choose from picking from gallery or taking a picture, then starts the corresponding activity.
     *
     * @param v image view
     */
    public void onImageClick(View v) {
        // Create a builder for generating the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set title for the dialog
        builder.setTitle(R.string.imgDialogTitle);
        // Generate options.
        String[] options = {getString(R.string.txtGallery), getString(R.string.txtCamera), getString(R.string.txtDelete)};
        // Set items and its onClick function.
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // gallery selected
                        Intent intentGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*");
                        startActivityForResult(intentGallery, GALLERY_IMAGE_REQUEST);
                        break;
                    case 1: // camera selected
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
                        break;
                    case 2: // delete selected
                        imgFile.delete();
                        imgProfile.setImageResource(R.drawable.usr);
                }
            }
        });
        // Create dialog.
        AlertDialog dialog = builder.create();
        // Show dialog.
        dialog.show();
    }


}