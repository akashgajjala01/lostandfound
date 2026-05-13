# Lost and Found Map Mobile App

## Project Overview

This is an Android mobile application developed for the Lost and Found app task. The main purpose of this app is to help users post lost or found items and view those items on a Google Map. The app allows users to enter item details, select or use their current location, and display nearby lost or found items based on distance.

This project extends the earlier Lost and Found app by adding location-based features using Google Maps. It helps users quickly understand where an item was lost or found, instead of only reading text details.

## Main Features

- Add lost or found item details.
- Store item information using SQLite database.
- Save item location using latitude and longitude.
- Use the user's current location.
- Display lost and found items on Google Map.
- Show sample markers such as Deakin Burwood Campus and Melbourne Airport.
- Radius-based search to show only items within selected distance.
- Simple and easy-to-use Android interface.

## Technologies Used

- Android Studio
- Kotlin
- SQLite Database
- Google Maps SDK
- Google Location Services
- XML Layouts
- Gradle

## App Screens

The app includes the following main screens:

1. Home screen  
   The user can choose to add an item or view items on the map.

2. Add item screen  
   The user can enter item information such as title, description, type, and location.

3. Map screen  
   The app displays lost and found items as markers on Google Maps.

4. Radius search  
   The app filters map markers and shows only items within the selected distance from the user's current location.

## How the App Works

When the user adds a lost or found item, the app stores the item information in the local SQLite database. The location is saved using latitude and longitude values. The user can use the current location button to capture their current location. Later, when the user opens the map screen, all saved items are displayed on the map as markers.

For testing and demonstration, extra markers such as Deakin Burwood Campus and Melbourne Airport are also included. This helps show that the map can display multiple locations and that radius-based filtering works properly.

## Radius-Based Search

The radius-based search feature allows users to search for items within a selected distance. The app compares the user's current location with the saved item locations. If an item is inside the selected radius, it is shown on the map. If it is outside the radius, it is hidden.

This feature is useful because users normally want to find items near their current area instead of seeing all items from far locations.

## Google Maps API

This app uses Google Maps API to display the map and markers. A valid Google Maps API key is required in the AndroidManifest.xml file.

Example:

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_GOOGLE_MAPS_API_KEY" />
