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
	
	static String currentTarget ="";
	static String currentChar ="";
	static String currentProg ="";
	
	static ResponseHandler<String> handler = new BasicResponseHandler();
	
	// mostly from stackoverflow.com
	
	public static String connect(String urlParameters) throws HttpResponseException {
		targetURL = "http://" + UI.getUrl() + "/join";
		Main.httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(targetURL);

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("client_id", rot13(UI.clientName.getText())));
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Execute and get the response.
		try {
			response = Main.httpclient.execute(httppost);
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
		HttpPost httppost = new HttpPost(targetURL);
		Main.httpclient = HttpClients.createDefault();
		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("client_id", rot13(UI.clientName.getText())));
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Execute and get the response.
		try {
			response = Main.httpclient.execute(httppost);
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
				Main.ThreadExists = false;
			}
			
		    try {
		        // do something useful
		    } finally {
		        try {
					instream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Main.ThreadExists = false;
				}
		    }
		}
		return responseString;
	}
	
	public static void start() throws HttpResponseException {
		if (Main.Connected && !Main.Stopped && !Main.Paused) {
			UI.setStatus("Making request");
			Main.httpclient = HttpClients.createDefault();
			targetURL = "http://" + UI.getUrl() + "/start_crack";
			HttpPost httppost = new HttpPost(targetURL);
	
			// Request parameters and other properties.
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("client_id", rot13(UI.hardClientName)));
			try {
				httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Main.ThreadExists = false;
			}
	
			//Execute and get the response.
			try {
				response = Main.httpclient.execute(httppost);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Main.ThreadExists = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Main.ThreadExists = false;
			}
			entity = response.getEntity();
	
			if (entity != null) {
			   
				try {
					responseString = handler.handleResponse(response);
					instream = entity.getContent();
				} catch (UnsupportedOperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Main.ThreadExists = false;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Main.ThreadExists = false;
				}
				
			    try {
			        // do something useful
			    } finally {
			        try {
						instream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Main.ThreadExists = false;
					}
			    }
			}
			JSONObject obj;
			String target = "";
			try {
				
				obj = new JSONObject(responseString);
				target = rot13(obj.get("target").toString());
				if (!target.contains("No hashes to solve")) {
					UI.setStatus("Generating hashes..");
					JSONArray arr = new JSONArray(obj.get("words").toString());
					UI.statusWindow.append("\nGot " + arr.length() + " new words. Starting with " + arr.get(0).toString());
					currentTarget = rot13(obj.get("target").toString());
					currentChar = rot13(obj.get("char").toString());
					currentProg = rot13(obj.get("progress").toString());
					UI.refresh();
					Main.Working = true;
					//UI.statusWindow.append("\nCreating hashes...");
					for (int i = 0; i < arr.length(); i++) {
						//System.out.println("target = " + target + " word = " + arr.getString(i) + " hash = " + MD5.getMD5(arr.getString(i)));
						if (MD5.getMD5(arr.getString(i)).equals(target)) {
							UI.statusWindow.append("\nFound solution! : " + arr.getString(i));
							found(target, arr.getString(i), UI.hardClientName);
							Main.solutions.put(target, arr.getString(i));
							Main.stop();
							UI.setStatus("Found solution!");
							break;
						}
					}
				}
				//UI.statusWindow.append("\nDidn't find it :(");
			} catch (JSONException e) {
				Main.Working = false;
				UI.statusWindow.append("\nBad response");
				Main.ThreadExists = false;
				UI.setStatus("Could not read JSON");
				e.printStackTrace();
			}
			
			UI.refresh();
			Main.Working = false;
			if (!Main.ThreadExists) new Thread(new Runnable() {
				@Override
				public void run() {
					if (!Main.Working && Main.Connected && !Main.Paused && !Main.Stopped) 
						try {next();} catch (HttpResponseException e) {e.printStackTrace();}}
			}).start();
			Main.ThreadExists = false;
		}
	}
	
	public static void next() throws HttpResponseException {
		
		if (Main.Connected && !Main.Stopped && !Main.Paused) {
			UI.setStatus("Getting next words..");
			Main.httpclient = HttpClients.createDefault();
			Main.ThreadExists = true;
			//UI.statusWindow.append("\nLets continue working!");
			targetURL = "http://" + UI.getUrl() + "/next";
			HttpPost httppost = new HttpPost(targetURL);
	
			// Request parameters and other properties.
			List<NameValuePair> params = new ArrayList<NameValuePair>(5);
			params.add(new BasicNameValuePair("client_id", rot13(UI.hardClientName)));
			params.add(new BasicNameValuePair("target", rot13(currentTarget)));
			params.add(new BasicNameValuePair("character", rot13(currentChar)));
			params.add(new BasicNameValuePair("progress", rot13(currentProg)));
			try {
				httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Main.ThreadExists = false;
			}
	
			//Execute and get the response.
			try {
				response = Main.httpclient.execute(httppost);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Main.ThreadExists = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Main.ThreadExists = false;
			}
			entity = response.getEntity();
	
			if (entity != null) {
			   
				try {
					responseString = handler.handleResponse(response);
					instream = entity.getContent();
				} catch (UnsupportedOperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Main.ThreadExists = false;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Main.ThreadExists = false;
				}
				
			    try {
			        // do something useful
			    } finally {
			        try {
						instream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Main.ThreadExists = false;
					}
			    }
			}
			JSONObject obj;
			String target = "";
			try {
				obj = new JSONObject(responseString);
				target = rot13(obj.get("target").toString());
				if (!target.contains("No hashes to solve")) {
					UI.setStatus("Generating hashes..");
					JSONArray arr = new JSONArray(rot13(obj.get("words").toString()));
					UI.statusWindow.append("\nGot " + arr.length() + " new words. Starting with " + arr.get(0).toString());
					UI.refresh();
					currentTarget = rot13(obj.get("target").toString());
					currentChar = rot13(obj.get("char").toString());
					currentProg = rot13(obj.get("progress").toString());
					Main.Working = true;
					for (int i = 0; i < arr.length(); i++) {
						if (MD5.getMD5(arr.getString(i)).equals(target)) {
							UI.statusWindow.append("\nFound solution! : " + arr.getString(i));
							UI.setStatus("Found solution!");
							found(target, arr.getString(i), UI.hardClientName);
							Main.solutions.put(target, arr.getString(i));
							Main.stop();
							break;
						}
						
					}
				}
				//UI.statusWindow.append("\nDidn't find it :(");
			} catch (JSONException e) {
				Main.Working = false;
				e.printStackTrace();
				Main.ThreadExists = false;
				UI.statusWindow.append("\nBad response");
				UI.setStatus("Could not read JSON");
			}
			
			UI.refresh();
			Main.Working = false;
			
			if (!Main.ThreadExists && !target.equals("No hashes to solve")) new Thread(new Runnable() {
				@Override
				public void run() {
					if (!Main.Working && Main.Connected && !Main.Paused && !Main.Stopped) 
						try {next();} catch (HttpResponseException e) {e.printStackTrace();}
				}
			}).start();
			Main.ThreadExists = false;
		}
	}
	
	public static void found(final String target, final String solution, final String client_id) {
		if (!Main.ThreadExists) new Thread(new Runnable() {
			@Override
			public void run() {
				UI.setStatus("Sending found information..");
				Main.httpclient = HttpClients.createDefault();
				Main.ThreadExists = true;
				targetURL = "http://" + UI.getUrl() + "/found";
				HttpPost httppost = new HttpPost(targetURL);

				// Request parameters and other properties.
				List<NameValuePair> params = new ArrayList<NameValuePair>(2);
				params.add(new BasicNameValuePair("client_id", rot13(UI.hardClientName)));
				params.add(new BasicNameValuePair("target", rot13(target)));
				params.add(new BasicNameValuePair("solution", rot13(solution)));
				
				try {
					httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Main.ThreadExists = false;
				}

				//Execute and get the response.
				try {
					Main.httpclient.execute(httppost);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Main.ThreadExists = false;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Main.ThreadExists = false;
				}
				
				Main.ThreadExists = false;
				Main.solutions.remove(target);
				UI.setStatus("Solution sent!");
			}
		}).start();
		
	}
	
	public static void sendPing() {
		UI.statusWindow.append("\nPinging server!");
		UI.refresh();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Main.ThreadExists = true;
				
				Main.httpclient = HttpClients.createDefault();
				targetURL = "http://" + UI.getUrl() + "/ping";
				
				HttpPost httppost = new HttpPost(targetURL);
				
				// Request parameters and other properties.
				List<NameValuePair> params = new ArrayList<NameValuePair>(2);
				params.add(new BasicNameValuePair("client_id", rot13(UI.hardClientName)));
				
				try {
					httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Main.ThreadExists = false;
				}

				//Execute and get the response.
				try {
					Main.httpclient.execute(httppost);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Main.ThreadExists = false;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Main.ThreadExists = false;
				}
				Main.ThreadExists = false;
				try {
					Main.httpclient.close();
				} catch (IOException e) {e.printStackTrace();}
				
			}
		}).start();
		
		
	}
	
	public static void keepAlive() {
		Main.httpclient.getConnectionManager().closeExpiredConnections();
		if (!Main.Working && Main.Connected && !Main.Stopped && !Main.Paused) {
			try {next();} catch (HttpResponseException e1) {e1.printStackTrace();}
		}
		else if (Main.Connected) {
			HTTPconnector.sendPing();
		}
		
	}
	
	public static String rot13(String input) {
	   StringBuilder sb = new StringBuilder();
	   for (int i = 0; i < input.length(); i++) {
	       char c = input.charAt(i);
	       if       (c >= 'a' && c <= 'm') c += 13;
	       else if  (c >= 'A' && c <= 'M') c += 13;
	       else if  (c >= 'n' && c <= 'z') c -= 13;
	       else if  (c >= 'N' && c <= 'Z') c -= 13;
	       sb.append(c);
	   }
	   return sb.toString();
	}
	
}

