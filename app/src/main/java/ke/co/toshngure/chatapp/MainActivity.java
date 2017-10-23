package ke.co.toshngure.chatapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ke.co.toshngure.basecode.app.BaseAppActivity;
import ke.co.toshngure.chatapp.model.User;
import ke.co.toshngure.chatsdk.ConversationBuilder;

public class MainActivity extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startSample(View view) {
        User user = new User();
        user.setId(1);
        user.setAvatar("https://lorempixel.com/400/400/?54444");
        user.setName("Anthony Ngure");

        User partner = new User();
        partner.setId(2);
        partner.setAvatar("https://lorempixel.com/400/400/?43496");
        partner.setName("Carlistars Wanga");

        new ConversationBuilder()
                .withUser(user)
                .withPartner(partner)
                .startConversation(this);
    }
}
