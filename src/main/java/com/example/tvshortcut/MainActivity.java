package com.example.tvshortcut;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = "https://www.google.com/search?q=90p&sca_esv=018739d33e04bd0c&sxsrf=APpeQnsR97IP44IZlXhxAmIRnjrg6B5MIw%3A1792900454325&source=hp&ei=Zn3dapHoEa61hvcPqLfMsQg&iflsig=ABILxe8AAAAAat2Ldtm1d-gZV6-fMEe6D_YU0rsZWGQF&ved=0ahUKEwiRs8jVotSXAxWumuEIHagbM4YQ4dUDCCk&uact=5&oq=90p";

        try {
            // Tạo intent mở link web chung
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Bắt buộc hiện bảng chọn trình duyệt (App Chooser)
            Intent chooser = Intent.createChooser(browserIntent, "Chọn trình duyệt mở 90p:");
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(chooser);
        } catch (Exception e) {
            Toast.makeText(this, "Không tìm thấy trình duyệt nào trên TV!", Toast.LENGTH_LONG).show();
        }

        finish();
    }
}
