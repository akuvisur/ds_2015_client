package ds2015_client;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HTTPconnector {
	static String responseString;
	
	static String targetURL;
	static HttpResponse response = null;
	static HttpEntity entity = null;
	static InputStream instream = null;
	
	static ResponseHandler<String> handler = new BasicResponseHandler();
	
	// mostly from stackoverflow.com
	
	public static String connect(String urlParameters) throws HttpResponseException {
		targetURL = "http://" + UI.getUrl() + "/join";
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(targetURL);

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("client_id", UI.clientName.getText()));
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Execute and get the response.
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		entity = response.getEntity();

		if (entity != null) {
		   
			try {
				responseString = handler.handleResponse(response);
				instream = entity.getContent();
			} catch (UnsupportedOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    try {
		        // do something useful
		    } finally {
		        try {
					instream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}
		return responseString;
	}
	
	public static String disconnect(String urlParameters) throws HttpResponseException {
		targetURL = "http://" + UI.getUrl() + "/disconnect";
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(targetURL);

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("client_id", UI.clientName.getText()));
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Execute and get the response.
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		entity = response.getEntity();

		if (entity != null) {
		   
			try {
				responseString = handler.handleResponse(response);
				instream = entity.getContent();
			} catch (UnsupportedOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    try {
		        // do something useful
		    } finally {
		        try {
					instream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}
		return responseString;
	}
	
	public static void start() throws HttpResponseException {
		UI.statusWindow.append("\nLets get to work!" + UI.hardClientName);
		targetURL = "http://" + UI.getUrl() + "/start_crack";
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(targetURL);

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("client_id", UI.hardClientName));
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Execute and get the response.
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		entity = response.getEntity();

		if (entity != null) {
		   
			try {
				responseString = handler.handleResponse(response);
				instream = entity.getContent();
			} catch (UnsupportedOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    try {
		        // do something useful
		    } finally {
		        try {
					instream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}
		UI.statusWindow.append("\n" + responseString);
		
		try {
			JSONObject obj = new JSONObject(responseString);
			String target = obj.get("target").toString();
			JSONArray arr = new JSONArray(obj.get("words").toString());
			for (int i = 0; i < arr.length(); i++) {
				System.out.println(arr.getString(i) + ", hash = " + MD5.getMD5(arr.getString(i)));
				if (MD5.getMD5(arr.getString(i)).equals(target)) {
					System.out.println("found it");
				}
			}
		} catch (JSONException e) {e.printStackTrace();}
		
		UI.refresh();
	}
	
	public static void sendPing() {
		UI.statusWindow.append("\nPinging server!");
		UI.refresh();
	}
	
	public static void keepAlive() {
		if (!Main.Working) {
			try {start();} catch (HttpResponseException e1) {e1.printStackTrace();}
		}
		HTTPconnector.sendPing();
	}
	
}

