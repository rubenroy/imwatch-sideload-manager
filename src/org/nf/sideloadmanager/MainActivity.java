package org.nf.sideloadmanager;

import java.util.ArrayList;  
import java.util.Arrays;
import java.io.File;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.content.ComponentName;

public class MainActivity extends Activity {
	Button b1, b2;
	TextView t1, t2, t3;
	ListView lv;
	private ArrayAdapter<String> listAdapter;
	private String root="/sdcard/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v("Tag", "onCreate() started");
        
        b2 = (Button) findViewById(R.id.button2);

        b2.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
                Log.v("Tag", "button2 clicked");
        		Toast msg = Toast.makeText(getBaseContext(),
        				getString(R.string.start_app_manager), Toast.LENGTH_SHORT);
        		msg.show();
        		Intent intent = new Intent(Intent.ACTION_MAIN);
        	    intent.setComponent(ComponentName.unflattenFromString("com.android.settings/.ManageApplications"));
        	    intent.addCategory(Intent.CATEGORY_LAUNCHER);
        	    startActivity(intent);
        	}
        });

        lv = (ListView) findViewById(R.id.listView1);
        ArrayList<String> filesList = new ArrayList<String>();
        listAdapter = new ArrayAdapter<String>(this, R.layout.list_item, filesList);
    	
        lv.setAdapter( listAdapter );
        getDir(root);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override  
            public void onItemClick( AdapterView<?> parent, View item,   
                                     int position, long id) {
        		String filename = listAdapter.getItem( position );
        		Toast msg = Toast.makeText(getBaseContext(),
        				filename, Toast.LENGTH_SHORT);
        		msg.show();
                Log.v("selected", "root='" + root + "' filename='" + filename + "'");
            	File f = new File(root + filename);
                if (filename.equals("../")) {
                	File fp = new File(root);
                	if (fp.getParent() != null)
                		root = fp.getParent() + "/";
                	else
                		root = "/";
            		Log.v("newroot", root );
            		getDir(root);
                } else if (f.isDirectory()) {
                		root = f.getAbsolutePath() + "/";
                		Log.v("newdir", root + " is a directory");
                		getDir(root);
                } else {
                		Uri uri = Uri.fromFile(f);
                		Log.v("Tag", "file " + f.getAbsolutePath() + " is a file");
                		// Intent intent = new Intent(Intent.ACTION_VIEW);
                		Intent intent = new Intent();
                	    //intent.setData(Uri.parse("file:/" + f.getAbsolutePath()));
                		intent.setAction(android.content.Intent.ACTION_VIEW);
                	    //intent.setData(uri);
                	    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                	    // intent.addCategory(Intent.CATEGORY_LAUNCHER);
                	    startActivity(intent);
                }
        	}
        });
    }

    private void getDir(String dirPath) {
    	File f = new File(dirPath);
    	File[] files = f.listFiles();

        Log.v("getDir", dirPath);

        if (files == null) {
    		Toast msg = Toast.makeText(getBaseContext(),
    				"Unable to change dir", Toast.LENGTH_SHORT);
    		msg.show();
        	return;
        }
        listAdapter.clear();
        if (!dirPath.equals("/") && !dirPath.equals("//"))
        	listAdapter.add("../");
		for (int i=0; i < files.length; i++) {
    		File file = files[i];
    		if (file.isDirectory()) {
    			listAdapter.add(file.getName() + "/");
    		} else {
    			listAdapter.add(file.getName());
    		}
    	}
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
*/
}
