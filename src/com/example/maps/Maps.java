package com.example.maps;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;


@EActivity(R.layout.activity_maps)
@OptionsMenu(R.menu.maps)
public class Maps extends FragmentActivity implements
OnMarkerClickListener,
OnInfoWindowClickListener,
OnMarkerDragListener,
OnSeekBarChangeListener {
private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
private static final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
private static final LatLng ADELAIDE = new LatLng(-34.92873, 138.59995);
private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);

/** Demonstrates customizing the info window and/or its contents. */
class CustomInfoWindowAdapter implements InfoWindowAdapter {

// These a both viewgroups containing an ImageView with id "badge" and two TextViews with id
// "title" and "snippet".
private final View mWindow;
private final View mContents;

CustomInfoWindowAdapter() {
    mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
    mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
    //mOptions = (RadioGroup) findViewById(R.id.custom_info_window_options);
}

@Override
public View getInfoWindow(Marker marker) {
    //if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
        // This means that getInfoContents will be called.
      //  return null;
    //}
    render(marker, mWindow);
    return mWindow;
}

@Override
public View getInfoContents(Marker marker) {
    //if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents) {
        // This means that the default info contents will be used.
      //  return null;
    //}
    render(marker, mContents);
    return mContents;
}

private void render(Marker marker, View view) {
    int badge;
    // Use the equals() method on a Marker to check for equals.  Do not use ==.
    if (marker.equals(mBrisbane)) {
        badge = R.drawable.badge_qld;
    } else if (marker.equals(mAdelaide)) {
        badge = R.drawable.badge_sa;
    } else if (marker.equals(mSydney)) {
        badge = R.drawable.badge_nsw;
    } else if (marker.equals(mMelbourne)) {
        badge = R.drawable.badge_victoria;
    } else if (marker.equals(mPerth)) {
        badge = R.drawable.badge_wa;
    } else {
        // Passing 0 to setImageResource will clear the image view.
        badge = 0;
    }
    ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

    String title = marker.getTitle();
    TextView titleUi = ((TextView) view.findViewById(R.id.title));
    if (title != null) {
        // Spannable string allows us to edit the formatting of the text.
        SpannableString titleText = new SpannableString(title);
        titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
        titleUi.setText(titleText);
    } else {
        titleUi.setText("");
    }

    String snippet = marker.getSnippet();
    TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
    if (snippet != null && snippet.length() > 12) {
        SpannableString snippetText = new SpannableString(snippet);
        snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
        snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
        snippetUi.setText(snippetText);
    } else {
        snippetUi.setText("");
    }
}
}
@OptionsMenuItem
MenuItem action_refresh;
@RestService
UserManager userManager;
@RestService
GroupManager groupManager;

List<User> lstUser;

private GoogleMap mMap;

private Marker mPerth;
private Marker mSydney;
private Marker mBrisbane;
private Marker mAdelaide;
private Marker mMelbourne;

private Marker mMyPositionMarker;
private LatLng mMyPosition;

private final List<Marker> mMarkerRainbow = new ArrayList<Marker>();

private TextView mTopText;
//private SeekBar mRotationBar;
//private CheckBox mFlatBox;

private final Random mRandom = new Random();
/*
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu items for use in the action bar
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.maps, menu);
    return super.onCreateOptionsMenu(menu);
}
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_maps);

setUpMapIfNeeded();
}

@Override
protected void onResume() {
super.onResume();
setUpMapIfNeeded();
}*/
@AfterViews
void setUpMapIfNeeded() {
	Log.v("setUpMapIfNeeded", "ok");
// Do a null check to confirm that we have not already instantiated the map.
if (mMap == null) {
    // Try to obtain the map from the SupportMapFragment.
    mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
    // Check if we were successful in obtaining the map.
    if (mMap != null) {
    	
        setUpMap();
    	//getGroups();
    	//getUsers();
    }
}
}
@Background
void getUsers(){
	//09366da8-f940-4b3e-bbc7-3f9d1dbed7cc
	lstUser = userManager.getUsers("72509982-3117-4132-9f9e-f05ab6908e9d");
	if(lstUser != null)
	{
		for(int i = 0; i <lstUser.size();i++)
		{
			Log.v("user",lstUser.get(i).toString());
		}
	}
	this.RefreshMap();
}

@UiThread
void RefreshMap()
{
	LatLngBounds.Builder builder= new LatLngBounds.Builder();
	mMap.clear();
	if(mMyPosition!=null)
	{
		builder.include(mMyPosition);
		DecimalFormat decim = new DecimalFormat("0");
		String snippet = new String();
    	snippet= "Vitesse : "+decim.format(0);
    	snippet+= "Altitude : "+ decim.format(0);
    	
    	mMap.addMarker(new MarkerOptions()
    	.position(mMyPosition)
    	.title("Moi")
    	.snippet(snippet));
	}
	if(lstUser!=null)
	{
		for(User u : lstUser)
		{
			if(u.getLatitude()!=0.0&&u.getLongitude()!=0.0)
			{
				builder.include(new LatLng(u.getLatitude(),u.getLongitude()));
				
				DecimalFormat decim = new DecimalFormat("0");
				String snippet = new String();
		    	snippet= "Vitesse : "+decim.format(0);
		    	snippet+= "Altitude : "+ decim.format(0);
		    	LatLng position =new LatLng(u.getLatitude(), u.getLongitude()); 
		    	
		    	mMap.addMarker(new MarkerOptions()
		    	.position(position)
		    	.title(u.getName())
		    	.snippet(snippet));
			}
		}
	}
	LatLngBounds bounds = builder.build();

	mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
	
}
	

@OptionsItem({R.id.action_refresh})
void Refresh()
{
	this.getUsers();
	
}

//ebfff4e0-edcb-4bd5-a8c8-b8492e235b2f
@Background
void getGroups()
{
	List<Group> listGroup = groupManager.getGroups("e6f88481-d70f-4c2e-9b34-92234f647d16");
	if(listGroup != null)
	{
		for(int i = 0; i <listGroup.size();i++)
		{
			Log.v("group",listGroup.get(i).toString());
		}
	}
	
}
void setUpMap() {
	Log.v("setUpMap", "ok");
// Hide the zoom controls as the button panel will cover it.
mMap.getUiSettings().setZoomControlsEnabled(false);

// Add lots of markers to the map.
//addMarkersToMap();

addMyMarkerToMap();

// Setting an info window adapter allows us to change the both the contents and look of the
// info window.
mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

// Set listeners for marker events.  See the bottom of this class for their behavior.
mMap.setOnMarkerClickListener(this);
mMap.setOnInfoWindowClickListener(this);
mMap.setOnMarkerDragListener(this);

// Pan to see all markers in view.
// Cannot zoom to bounds until the map has a size.
/*final View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
if (mapView.getViewTreeObserver().isAlive()) {
    mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
        @SuppressWarnings("deprecation") // We use the new method when supported
        @SuppressLint("NewApi") // We check which build version we are using.
        @Override
        public void onGlobalLayout() {
        	LatLngBounds bounds;
        	if(mMyPosition!=null)
        	{
        		bounds= new LatLngBounds.Builder()
                    .include(PERTH)
                    .include(SYDNEY)
                    .include(ADELAIDE)
                    .include(BRISBANE)
                    .include(MELBOURNE)
                    .include(mMyPosition)
                    .build();
    		}
        	else
        	{
        		bounds= new LatLngBounds.Builder()
                .include(PERTH)
                .include(SYDNEY)
                .include(ADELAIDE)
                .include(BRISBANE)
                .include(MELBOURNE)
                .build();
        	}
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
              mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            } else {
              mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        }
    });
}*/
}

void addMyMarkerToMap() {
	
	LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	ArrayList<LocationProvider> providers = new ArrayList<LocationProvider>();
	
	Criteria critere = new Criteria();

	// Pour indiquer la précision voulue
	// On peut mettre ACCURACY_FINE pour une haute précision ou ACCURACY_COARSE pour une moins bonne précision
	critere.setAccuracy(Criteria.ACCURACY_FINE);

	// Est-ce que le fournisseur doit être capable de donner une altitude ?
	critere.setAltitudeRequired(true);

	// Est-ce que le fournisseur doit être capable de donner une direction ?
	critere.setBearingRequired(true);

	// Est-ce que le fournisseur peut être payant ?
	critere.setCostAllowed(false);

	// Pour indiquer la consommation d'énergie demandée
	// Criteria.POWER_HIGH pour une haute consommation, Criteria.POWER_MEDIUM pour une consommation moyenne et Criteria.POWER_LOW pour une basse consommation
	critere.setPowerRequirement(Criteria.POWER_HIGH);

	// Est-ce que le fournisseur doit être capable de donner une vitesse ?
	critere.setSpeedRequired(true);
	
	List<String> names = locationManager.getProviders(critere,true);

	//for(String name : names)
	//{
	  //providers.add(locationManager.getProvider(name));
	  locationManager.requestLocationUpdates(60000, 150,critere ,new LocationListener() {
	
		  @Override
		  public void onStatusChanged(String provider, int status, Bundle extras) {
			  Log.v("locationManager.onStatusChanged",provider);
		  }

		  @Override
		  public void onProviderEnabled(String provider) {
			  Log.v("locationManager.onProviderEnabled",provider);
		  }

		  @Override
		  public void onProviderDisabled(String provider) {
			  Log.v("locationManager.onProviderDisabled",provider);
		  }

		  @Override
		  public void onLocationChanged(Location location) {
			  Log.v("locationManager.onLocationChanged",location.getProvider());
			boolean positionChanged = false;
		    if(mMyPosition == null)
		    {
		    	positionChanged = true;
		    	mMyPosition =new LatLng(location.getLatitude(), location.getLongitude());
		    	
//		    	DecimalFormat decim = new DecimalFormat("0");
//		    	
//		    	String snippet = new String();
//		    	snippet= "Vitesse : "+decim.format(location.getSpeed());
//		    	snippet+= "Altitude : "+ decim.format(location.getAltitude());
//		    	 
//		    	mMyPositionMarker=mMap.addMarker(new MarkerOptions()
//		    	.position(mMyPosition)
//		    	.title("Moi")
//		    	.snippet(snippet));
		    }
		    else
		    {
		    	if(mMyPosition.longitude!= location.getLongitude()
		    			||mMyPosition.latitude!= location.getLatitude())
		    	{
		    		positionChanged = true;
		    		mMyPosition = new LatLng(location.getLatitude(), location.getLongitude());
		    		//mMyPositionMarker.setPosition(mMyPosition);
		    	}
		    }
		    if(positionChanged == true)
		    {
//		    	LatLngBounds bounds= new LatLngBounds.Builder()
//	                    .include(mMyPosition)
//	                    .build();
//
//		            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
		    	RefreshMap();
		    }
		  }
		},null);
	//}
}

private void addMarkersToMap() {
// Uses a colored icon.
mBrisbane = mMap.addMarker(new MarkerOptions()
        .position(BRISBANE)
        .title("Brisbane")
        .snippet("Population: 2,074,200")
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

// Uses a custom icon with the info window popping out of the center of the icon.
mSydney = mMap.addMarker(new MarkerOptions()
        .position(SYDNEY)
        .title("Sydney")
        .snippet("Population: 4,627,300")
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
        .infoWindowAnchor(0.5f, 0.5f));

// Creates a draggable marker. Long press to drag.
mMelbourne = mMap.addMarker(new MarkerOptions()
        .position(MELBOURNE)
        .title("Melbourne")
        .snippet("Population: 4,137,400")
        .draggable(true));

// A few more markers for good measure.
mPerth = mMap.addMarker(new MarkerOptions()
        .position(PERTH)
        .title("Perth")
        .snippet("Population: 1,738,800"));
mAdelaide = mMap.addMarker(new MarkerOptions()
        .position(ADELAIDE)
        .title("Adelaide")
        .snippet("Population: 1,213,000"));

// Creates a marker rainbow demonstrating how to create default marker icons of different
// hues (colors).
//float rotation = mRotationBar.getProgress();
//boolean flat = mFlatBox.isChecked();

int numMarkersInRainbow = 12;
for (int i = 0; i < numMarkersInRainbow; i++) {
    mMarkerRainbow.add(mMap.addMarker(new MarkerOptions()
            .position(new LatLng(
                    -30 + 10 * Math.sin(i * Math.PI / (numMarkersInRainbow - 1)),
                    135 - 10 * Math.cos(i * Math.PI / (numMarkersInRainbow - 1))))
            .title("Marker " + i)
            .icon(BitmapDescriptorFactory.defaultMarker(i * 360 / numMarkersInRainbow))));
}
}

private boolean checkReady() {
if (mMap == null) {
    Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
    return false;
}
return true;
}

/** Called when the Clear button is clicked. */
public void onClearMap(View view) {
if (!checkReady()) {
    return;
}
mMap.clear();
}

/** Called when the Reset button is clicked. */
public void onResetMap(View view) {
if (!checkReady()) {
    return;
}
// Clear the map because we don't want duplicates of the markers.
mMap.clear();
addMarkersToMap();
}

/** Called when the Reset button is clicked. */
public void onToggleFlat(View view) {
if (!checkReady()) {
    return;
}

}

@Override
public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
if (!checkReady()) {
    return;
}
float rotation = seekBar.getProgress();
for (Marker marker : mMarkerRainbow) {
    marker.setRotation(rotation);
}
}

@Override
public void onStartTrackingTouch(SeekBar seekBar) {
// Do nothing.
}

@Override
public void onStopTrackingTouch(SeekBar seekBar) {
// Do nothing.
}

//
// Marker related listeners.
//

@Override
public boolean onMarkerClick(final Marker marker) {
if (marker.equals(mPerth)) {
    // This causes the marker at Perth to bounce into position when it is clicked.
    final Handler handler = new Handler();
    final long start = SystemClock.uptimeMillis();
    final long duration = 1500;

    final Interpolator interpolator = new BounceInterpolator();

    handler.post(new Runnable() {
        @Override
        public void run() {
            long elapsed = SystemClock.uptimeMillis() - start;
            float t = Math.max(1 - interpolator
                    .getInterpolation((float) elapsed / duration), 0);
            marker.setAnchor(0.5f, 1.0f + 2 * t);

            if (t > 0.0) {
                // Post again 16ms later.
                handler.postDelayed(this, 16);
            }
        }
    });
} else if (marker.equals(mAdelaide)) {
    // This causes the marker at Adelaide to change color and alpha.
    marker.setIcon(BitmapDescriptorFactory.defaultMarker(mRandom.nextFloat() * 360));
    marker.setAlpha(mRandom.nextFloat());
}
// We return false to indicate that we have not consumed the event and that we wish
// for the default behavior to occur (which is for the camera to move such that the
// marker is centered and for the marker's info window to open, if it has one).
return false;
}

@Override
public void onInfoWindowClick(Marker marker) {
Toast.makeText(this, "Click Info Window", Toast.LENGTH_SHORT).show();
}

@Override
public void onMarkerDragStart(Marker marker) {
mTopText.setText("onMarkerDragStart");
}

@Override
public void onMarkerDragEnd(Marker marker) {
mTopText.setText("onMarkerDragEnd");
}

@Override
public void onMarkerDrag(Marker marker) {
mTopText.setText("onMarkerDrag.  Current Position: " + marker.getPosition());
}
}
