package com.example.simplewebbrowser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
	Button buttonGo;
	EditText editUrl;
	WebView webView;
	ProgressBar progressBar;
	boolean finished = false;
	double counter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		buttonGo = (Button) findViewById(R.id.buttonGo);
		buttonGo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (editUrl.getText().toString() == "") {

				} else {
					new LoadWebTask().execute(editUrl.getText().toString());
				}
			}
		});
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		editUrl = (EditText) findViewById(R.id.editUrl);
		webView = (WebView) findViewById(R.id.webView);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class LoadWebTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			Log.d("Masuk", urls[0]);
			String response = "";
			finished=false;
			for (String url : urls) {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse execute = client.execute(httpGet);
					InputStream content = execute.getEntity().getContent();

					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(content));
					String s = "";
					counter = 0;
					while ((s = buffer.readLine()) != null) {
						response += s;
						progressBar.setProgress((int)counter);
					}

				} catch (Exception e) {
					response = e.getMessage();
				}
			}
			finished=true;
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			webView.loadData(result, "text/html", "utf-8");
		}

	}
}
