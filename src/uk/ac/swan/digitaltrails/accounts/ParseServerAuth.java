package uk.ac.swan.digitaltrails.accounts;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

//TODO: waiting on thomas!
public class ParseServerAuth implements ServerAuthenticate {

	@Override
	public String userSignUp(String firstName, String lastName, String email,
			String pass, String authType) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String userSignIn(String user, String pass, String authType)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
