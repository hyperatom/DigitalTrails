package uk.ac.swan.digitaltrails.accounts;

import uk.ac.swan.digitaltrails.R;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpFragment extends Fragment {


	private String TAG = "SignUpActivity";
	private String mAccountType;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.act_register, container, false);
	}
	
	@Override
	public void onCreate(Bundle savedInstnaceState) {
		super.onCreate(savedInstnaceState);
		
		mAccountType = getActivity().getIntent().getStringExtra(AccountGeneral.ACCOUNT_TYPE);
			
		getView().findViewById(R.id.alreadyMember).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().setResult(Activity.RESULT_CANCELED);
				getActivity().finish();
			}
		});
		
		getView().findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				createAccount();
				
			}
		});
	}
	
	private void createAccount() {
		new AsyncTask<String, Void, Intent>() {
			
			String firstName = ((TextView) getView().findViewById(R.id.firstName)).getText().toString().trim();
			String lastName = ((TextView) getView().findViewById(R.id.lastName)).getText().toString().trim();
			String accountName = ((TextView) getView().findViewById(R.id.accountName)).getText().toString().trim();
			String accountPassword = ((TextView) getView().findViewById(R.id.accountPassword)).getText().toString().trim();
			
			@Override
			protected Intent doInBackground(String... arg0) {
				Log.d(TAG, "Started authenticating");
				
				String authtoken = null;
				Bundle data = new Bundle();
				try {
					authtoken = AccountGeneral.sServerAuthenticate.userSignUp(firstName, lastName, accountName, accountPassword, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
				
					data.putString(AccountManager.KEY_ACCOUNT_NAME, accountName);
					data.putString(AccountManager.KEY_ACCOUNT_TYPE, mAccountType);;
					data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
					data.putString(AuthenticatorActivity.USER_PASS, accountPassword);
				} catch (Exception e) {
					data.putString(AuthenticatorActivity.ERROR_MESSAGE, e.getMessage());
				}
				
				final Intent result = new Intent();
				result.putExtras(data);
				return result;
			}
			
			
			@Override
			protected void onPostExecute(Intent intent) {
				if (intent.hasExtra(AuthenticatorActivity.ERROR_MESSAGE)) {
					Toast.makeText(getActivity().getBaseContext(), intent.getStringExtra(AuthenticatorActivity.ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
				} else {
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
            
				}
			}
			
		}.execute(); 
	}
	
	public void doBackPressed() {
		getActivity().setResult(Activity.RESULT_CANCELED);
		getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

}
