package com.example.tvshortcut;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final String PREFS_NAME = "TVBrowserPrefs";
    private static final String KEY_PACKAGE = "saved_browser_package";
    private static final String URL = "https://www.google.com/search?q=90p&sca_esv=018739d33e04bd0c&sxsrf=APpeQnsR97IP44IZlXhxAmIRnjrg6B5MIw%3A1792900454325&source=hp&ei=Zn3dapHoEa61hvcPqLfMsQg&iflsig=ABILxe8AAAAAat2Ldtm1d-gZV6-fMEe6D_YU0rsZWGQF&ved=0ahUKEwiRs8jVotSXAxWumuEIHagbM4YQ4dUDCCk&uact=5&oq=90p";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedPackage = prefs.getString(KEY_PACKAGE, null);

        // Nếu đã từng chọn trước đó, mở thẳng
        if (savedPackage != null && isAppInstalled(savedPackage)) {
            openWithPackage(savedPackage);
            finish();
            return;
        }

        // Lần đầu: Tìm tất cả trình duyệt web đang có trên TV
        PackageManager pm = getPackageManager();
        Intent queryIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"));
        queryIntent.addCategory(Intent.CATEGORY_BROWSABLE);

        List<ResolveInfo> rawList = pm.queryIntentActivities(queryIntent, 0);
        List<ResolveInfo> browserList = new ArrayList<>();
        List<String> names = new ArrayList<>();

        for (ResolveInfo info : rawList) {
            // Bỏ qua chính app shortcut này
            if (!info.activityInfo.packageName.equals(getPackageName())) {
                browserList.add(info);
                names.add(info.loadLabel(pm).toString());
            }
        }

        if (browserList.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy trình duyệt nào trên TV!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Hiện hộp thoại chọn trình duyệt trên TV
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn trình duyệt (sẽ ghi nhớ cho các lần sau):");
        builder.setItems(names.toArray(new CharSequence[0]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String chosenPkg = browserList.get(which).activityInfo.packageName;
                // Lưu lựa chọn
                prefs.edit().putString(KEY_PACKAGE, chosenPkg).apply();
                openWithPackage(chosenPkg);
                finish();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    private void openWithPackage(String packageName) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
            intent.setPackage(packageName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi mở trình duyệt!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isAppInstalled(String packageName) {
        try {
            getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
