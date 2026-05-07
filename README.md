# Lost and Found Android App

## Project Overview

This project is a Lost and Found mobile application developed for **SIT708 Task 7.1**. The main purpose of the app is to help users report lost or found items and make it easier for owners to recover their belongings.

The app allows users to add item details, upload an image, view all saved posts, filter items by category, and delete an advert once the item has been returned to the owner.

## Features

The application includes the following features:

- Add a lost or found item
- Store item details using SQLite database
- Upload/select an image for each item
- Display all saved lost and found posts
- Search or filter items by category
- Show date and time for each post
- Delete an item advert after it is resolved
- Simple and user-friendly Android interface

## Technologies Used

- Android Studio
- Kotlin
- XML Layouts
- SQLite Database
- Android Emulator / Physical Android Device

## App Screens

The app contains the following main screens:

### Main Screen

The main screen gives users two options:

- Add a new lost/found item
- View existing lost/found items

### Add Item Screen

On this screen, users can enter details about the item, such as item name, category, description, location, contact details, and image. After entering the details, the information is saved into the SQLite database.

### View Items Screen

This screen displays all the saved lost and found posts. Users can view item details, filter posts by category, and delete an item once it has been returned to the owner.

## Database

The app uses SQLite to store the lost and found item details locally on the device. SQLite was used because it is simple, lightweight, and does not require an internet connection.

The database stores information such as:

- Item name
- Category
- Description
- Location
- Contact details
- Image path
- Date and time stamp

## How to Run the Project

1. Open Android Studio.
2. Click on **Open Project**.
3. Select the Lost and Found app project folder.
4. Wait for Gradle sync to finish.
5. Connect an Android device or start an emulator.
6. Click the **Run** button.
7. The app will open on the selected device/emulator.

## How the App Works

First, the user opens the app and chooses whether to add a new item or view existing items. When adding an item, the user enters the item details and selects an image. After saving, the item is stored in the SQLite database.

The user can then go to the View Items screen to see all saved posts. The filter option helps users search items based on category, such as electronics, pets, wallets, or other item types. If an item has been returned to the owner, the user can delete the advert from the list.

## Future Improvements

In the future, this app can be improved by adding more advanced features. A login system could be added so that each user can manage their own posts. Cloud storage could also be used to store data online instead of only on the local device. Map support could help users mark the exact location where the item was lost or found. Push notifications could also be added to alert users when a matching item is posted.

## Learning Outcome

This project helped me understand how to build an Android application using Kotlin and XML. It also helped me learn how to use SQLite for local data storage, how to move between different activities, how to handle user input, and how to display saved data in the app.

## Author

Akash Reddy Gajjala
