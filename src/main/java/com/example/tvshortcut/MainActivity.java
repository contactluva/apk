package com.example.tvshortcut;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link Google 90p của bạn
        String url = "https://www.google.com/search?q=90p&sca_esv=018739d33e04bd0c&sxsrf=APpeQnsR97IP44IZlXhxAmIRnjrg6B5MIw%3A1792900454325&source=hp&ei=Zn3dapHoEa61hvcPqLfMsQg&iflsig=ABILxe8AAAAAat2Ldtm1d-gZV6-fMEe6D_YU0rsZWGQF&ved=0ahUKEwiRs8jVotSXAxWumuEIHagbM4YQ4dUDCCk&uact=5&oq=90p";

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            // Ép mở bằng TV Bro (Package name là com.phlox.tvwebbrowser)
            intent.setPackage("com.phlox.tvwebbrowser");
            startActivity(intent);
        } catch (Exception e) {
            // Dự phòng: Nếu TV Bro chưa cài, mở bằng trình duyệt mặc định
            Intent fallback = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(fallback);
        }
        finish(); // Tắt app shortcut ngay lập tức
    }
}
