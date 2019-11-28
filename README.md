Please read through the following points before using the application:

1. Run this application using Android Studio API version 28+
2. The home screen will give you 3 options which represent the 3 main user flows which should be reasonably self-explanatory. You have the option to scan a barcode (using the camera), enter a barcode by typing it in manually or search through a database of products organised by category. For a further explanation of the  of how the application works, refer to the accompanying paper: “Sustainly, a sustainability focused Android Application”
3. If using an emulator to test the application, note that the geolocation features will not work correctly as the emulator defaults to San Francisco, California. You can change this in the emulator settings. These features will work properly if running the app on a phone.
4. If you would like enter a few barcodes as a test, here is a list of a few that are currently in the application’s database:

        Barcode | Product | Manufacturing Location | Origin of ingredients | Category

        4311501606704 | Steak Burger | France | France, Germany | Meat

        5099630250997 | Brown soda bread | Ireland | Ireland, United Kingdom | Bread

        3590941000926 | Carrots | France | France | Vegetable

        5011096004174 | Tayto Popcorn | Ireland | Ireland | Snacks


5. For any barcodes that aren’t in the database the application will call an external API to request relevant information. In order to test this you can try any barcode that is listed at the following API: https://world.openfoodfacts.org/data however be aware that because this API is free and maintained a voluntary community, there may be missing information.

6. The application has been testing for situations where no wifi is available to a user, permission hasn’t been provided for geolocation and no information has been returned from the external API or local database.

7. The application meets the 5 items outlined in the specification: 
---1. There are lists, buttons and toasts in the application
---2. There are more than 7 distinct activities
---3. The application uses a SQLite database to store barcode information
---4. The application uses 3 sensors: geolocation, camera and api
---5. The application connects with camera and maps applications
