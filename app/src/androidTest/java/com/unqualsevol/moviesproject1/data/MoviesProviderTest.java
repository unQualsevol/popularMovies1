package com.unqualsevol.moviesproject1.data;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import static com.unqualsevol.moviesproject1.data.TestUtilities.BULK_INSERT_RECORDS_TO_INSERT;
import static com.unqualsevol.moviesproject1.data.TestUtilities.MOVIE_ID_TEST;
import static com.unqualsevol.moviesproject1.data.TestUtilities.createBulkInsertTestMoviesValues;
import static com.unqualsevol.moviesproject1.data.TestUtilities.createTestMovieContentValues;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

//TODO: remove all comments and fix names
public class MoviesProviderTest {

    /* Context used to access various parts of the system */
    private final Context mContext = InstrumentationRegistry.getTargetContext();

    /**
     * Because we annotate this method with the @Before annotation, this method will be called
     * before every single method with an @Test annotation. We want to start each test clean, so we
     * delete all entries in the movies table to do so.
     */
    @Before
    public void setUp() {
        deleteAllRecordsFromMoviesTable();
    }

    /**
     * This test checks to make sure that the content provider is registered correctly in the
     * AndroidManifest file. If it fails, you should check the AndroidManifest to see if you've
     * added a <provider/> tag and that you've properly specified the android:authorities attribute.
     * <p>
     * Potential causes for failure:
     * <p>
     *   1) Your MoviesProvider was registered with the incorrect authority
     * <p>
     *   2) Your MoviesProvider was not registered at all
     */
    @Test
    public void testProviderRegistry() {

        /*
         * A ComponentName is an identifier for a specific application component, such as an
         * Activity, ContentProvider, BroadcastReceiver, or a Service.
         *
         * Two pieces of information are required to identify a component: the package (a String)
         * it exists in, and the class (a String) name inside of that package.
         *
         * We will use the ComponentName for our ContentProvider class to ask the system
         * information about the ContentProvider, specifically, the authority under which it is
         * registered.
         */
        String packageName = mContext.getPackageName();
        String MoviesProviderClassName = MoviesProvider.class.getName();
        ComponentName componentName = new ComponentName(packageName, MoviesProviderClassName);

        try {

            /*
             * Get a reference to the package manager. The package manager allows us to access
             * information about packages installed on a particular device. In this case, we're
             * going to use it to get some information about our ContentProvider under test.
             */
            PackageManager pm = mContext.getPackageManager();

            /* The ProviderInfo will contain the authority, which is what we want to test */
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            String actualAuthority = providerInfo.authority;
            String expectedAuthority = MoviesContract.CONTENT_AUTHORITY;

            /* Make sure that the registered authority matches the authority from the Contract */
            String incorrectAuthority =
                    "Error: MoviesProvider registered with authority: " + actualAuthority +
                            " instead of expected authority: " + expectedAuthority;
            assertEquals(incorrectAuthority,
                    actualAuthority,
                    expectedAuthority);

        } catch (PackageManager.NameNotFoundException e) {
            String providerNotRegisteredAtAll =
                    "Error: MoviesProvider not registered at " + mContext.getPackageName();
            /*
             * This exception is thrown if the ContentProvider hasn't been registered with the
             * manifest at all. If this is the case, you need to double check your
             * AndroidManifest file
             */
            fail(providerNotRegisteredAtAll);
        }
    }

    /**
     * This test uses the database directly to insert a row of test data and then uses the
     * ContentProvider to read out the data. We access the database directly to insert the data
     * because we are testing our ContentProvider's query functionality. If we wanted to use the
     * ContentProvider's insert method, we would have to assume that that insert method was
     * working, which defeats the point of testing.
     * <p>
     * If this test fails, you should check the logic in your
     * {@link MoviesProvider#insert(Uri, ContentValues)} and make sure it matches up with our
     * solution code.
     * <p>
     * Potential causes for failure:
     * <p>
     *   1) There was a problem inserting data into the database directly via SQLite
     * <p>
     *   2) The values contained in the cursor did not match the values we inserted via SQLite
     */
    @Test
    public void testBasicmoviesQuery() {

        /* Use moviesDbHelper to get access to a writable database */
        MoviesDbHelper dbHelper = new MoviesDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        /* Obtain movies values from TestUtilities */
        ContentValues testmoviesValues = TestUtilities.createTestMovieContentValues();

        /* Insert ContentValues into database and get a row ID back */
        long moviesRowId = database.insert(
                /* Table to insert values into */
                MoviesContract.MovieEntry.TABLE_NAME,
                null,
                /* Values to insert into table */
                testmoviesValues);

        String insertFailed = "Unable to insert into the database";
        assertTrue(insertFailed, moviesRowId != -1);

        /* We are done with the database, close it now. */
        database.close();

        /*
         * Perform our ContentProvider query. We expect the cursor that is returned will contain
         * the exact same data that is in testmoviesValues and we will validate that in the next
         * step.
         */
        Cursor moviesCursor = mContext.getContentResolver().query(
                MoviesContract.MovieEntry.CONTENT_URI,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Sort order to return in Cursor */
                null);

        /* This method will ensure that we  */
        TestUtilities.validateThenCloseCursor("testBasicmoviesQuery",
                moviesCursor,
                testmoviesValues);
    }
    @Test
    public void testBasicmoviesQueryWithMovieId() {

        /* Use moviesDbHelper to get access to a writable database */
        MoviesDbHelper dbHelper = new MoviesDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        /* Obtain movies values from TestUtilities */
        ContentValues testmoviesValues = TestUtilities.createTestMovieContentValues();

        /* Insert ContentValues into database and get a row ID back */
        long moviesRowId = database.insert(
                /* Table to insert values into */
                MoviesContract.MovieEntry.TABLE_NAME,
                null,
                /* Values to insert into table */
                testmoviesValues);

        String insertFailed = "Unable to insert into the database";
        assertTrue(insertFailed, moviesRowId != -1);

        /* We are done with the database, close it now. */
        database.close();

        /*
         * Perform our ContentProvider query. We expect the cursor that is returned will contain
         * the exact same data that is in testmoviesValues and we will validate that in the next
         * step.
         */
        Cursor moviesCursor = mContext.getContentResolver().query(
                MoviesContract.MovieEntry.buildMovieUriWithId(MOVIE_ID_TEST),
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Sort order to return in Cursor */
                null);

        /* This method will ensure that we  */
        TestUtilities.validateThenCloseCursor("testBasicmoviesQuery",
                moviesCursor,
                testmoviesValues);
    }

    @Test
    public void testInsert() {

        /* Create a new array of ContentValues for movies */
        ContentValues insertTestContentValues = createTestMovieContentValues();

        /*
         * TestContentObserver allows us to test movies or not notifyChange was called
         * appropriately. We will use that here to make sure that notifyChange is called when a
         * deletion occurs.
         */
        TestUtilities.TestContentObserver moviesObserver = TestUtilities.getTestContentObserver();

        /*
         * A ContentResolver provides us access to the content model. We can use it to perform
         * deletions and queries at our CONTENT_URI
         */
        ContentResolver contentResolver = mContext.getContentResolver();

        /* Register a content observer to be notified of changes to data at a given URI (movies) */
        contentResolver.registerContentObserver(
                /* URI that we would like to observe changes to */
                MoviesContract.MovieEntry.CONTENT_URI,
                /* Whether or not to notify us if descendants of this URI change */
                true,
                /* The observer to register (that will receive notifyChange callbacks) */
                moviesObserver);

        Uri actualUri = contentResolver.insert(
                /* URI at which to insert data */
                MoviesContract.MovieEntry.CONTENT_URI,
                insertTestContentValues);


        Uri expectedUri = ContentUris.withAppendedId(MoviesContract.MovieEntry.CONTENT_URI, 1);

        String insertProviderFailed = "Unable to insert item through Provider";
        assertEquals(insertProviderFailed, expectedUri, actualUri);

        /*
         * If this fails, it's likely you didn't call notifyChange in your insert method from
         * your ContentProvider.
         */
        moviesObserver.waitForNotificationOrFail();

        /*
         * waitForNotificationOrFail is synchronous, so after that call, we are done observing
         * changes to content and should therefore unregister this observer.
         */
        contentResolver.unregisterContentObserver(moviesObserver);

        /*
         * Perform our ContentProvider query. We expect the cursor that is returned will contain
         * the exact same data that is in testmoviesValues and we will validate that in the next
         * step.
         */
        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.MovieEntry.buildMovieUriWithId(MOVIE_ID_TEST),
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Sort criteria */
                null);

        /*
         * Although we already tested the number of records that the ContentProvider reported
         * inserting, we are now testing the number of records that the ContentProvider actually
         * returned from the query above.
         */
        assertEquals(cursor.getCount(), 1);

        /*
         * We now loop through and validate each record in the Cursor with the expected values from
         * insertTestContentValues.
         */
        cursor.moveToFirst();
            TestUtilities.validateCurrentRecord(
                    "testBulkInsert. Error validating MovieEntry ",
                    cursor,
                    insertTestContentValues);

        /* Always close the Cursor! */
        cursor.close();
    }
    /**
     * This test test the bulkInsert feature of the ContentProvider. It also verifies that
     * registered ContentObservers receive onChange callbacks when data is inserted.
     * <p>
     * It finally queries the ContentProvider to make sure that the table has been successfully
     * inserted.
     * <p>
     * Potential causes for failure:
     * <p>
     *   1) Within {@link MoviesProvider#delete(Uri, String, String[])}, you didn't call
     *    getContext().getContentResolver().notifyChange(uri, null) after performing an insertion.
     * <p>
     *   2) The number of records the ContentProvider reported that it inserted do no match the
     *    number of records we inserted into the ContentProvider.
     * <p>
     *   3) The size of the Cursor returned from the query does not match the number of records
     *    that we inserted into the ContentProvider.
     * <p>
     *   4) The data contained in the Cursor from our query does not match the data we inserted
     *    into the ContentProvider.
     * </p>
     */
    @Test
    public void testBulkInsert() {

        /* Create a new array of ContentValues for movies */
        ContentValues[] bulkInsertTestContentValues = createBulkInsertTestMoviesValues();

        /*
         * TestContentObserver allows us to test movies or not notifyChange was called
         * appropriately. We will use that here to make sure that notifyChange is called when a
         * deletion occurs.
         */
        TestUtilities.TestContentObserver moviesObserver = TestUtilities.getTestContentObserver();

        /*
         * A ContentResolver provides us access to the content model. We can use it to perform
         * deletions and queries at our CONTENT_URI
         */
        ContentResolver contentResolver = mContext.getContentResolver();

        /* Register a content observer to be notified of changes to data at a given URI (movies) */
        contentResolver.registerContentObserver(
                /* URI that we would like to observe changes to */
                MoviesContract.MovieEntry.CONTENT_URI,
                /* Whether or not to notify us if descendants of this URI change */
                true,
                /* The observer to register (that will receive notifyChange callbacks) */
                moviesObserver);

        /* bulkInsert will return the number of records that were inserted. */
        int insertCount = contentResolver.bulkInsert(
                /* URI at which to insert data */
                MoviesContract.MovieEntry.CONTENT_URI,
                /* Array of values to insert into given URI */
                bulkInsertTestContentValues);

        /*
         * If this fails, it's likely you didn't call notifyChange in your insert method from
         * your ContentProvider.
         */
        moviesObserver.waitForNotificationOrFail();

        /*
         * waitForNotificationOrFail is synchronous, so after that call, we are done observing
         * changes to content and should therefore unregister this observer.
         */
        contentResolver.unregisterContentObserver(moviesObserver);

        /*
         * We expect that the number of test content values that we specify in our TestUtility
         * class were inserted here. We compare that value to the value that the ContentProvider
         * reported that it inserted. These numbers should match.
         */
        String expectedAndActualInsertedRecordCountDoNotMatch =
                "Number of expected records inserted does not match actual inserted record count";
        assertEquals(expectedAndActualInsertedRecordCountDoNotMatch,
                insertCount,
                BULK_INSERT_RECORDS_TO_INSERT);

        /*
         * Perform our ContentProvider query. We expect the cursor that is returned will contain
         * the exact same data that is in testmoviesValues and we will validate that in the next
         * step.
         */
        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.MovieEntry.CONTENT_URI,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Sort by date from smaller to larger (past to future) */
                MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " ASC");

        /*
         * Although we already tested the number of records that the ContentProvider reported
         * inserting, we are now testing the number of records that the ContentProvider actually
         * returned from the query above.
         */
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        /*
         * We now loop through and validate each record in the Cursor with the expected values from
         * bulkInsertTestContentValues.
         */
        cursor.moveToFirst();
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext()) {
            TestUtilities.validateCurrentRecord(
                    "testBulkInsert. Error validating MovieEntry " + i,
                    cursor,
                    bulkInsertTestContentValues[i]);
        }

        /* Always close the Cursor! */
        cursor.close();
    }

    /**
     * This test deletes all records from the movies table using the ContentProvider. It also
     * verifies that registered ContentObservers receive onChange callbacks when data is deleted.
     * <p>
     * It finally queries the ContentProvider to make sure that the table has been successfully
     * cleared.
     * <p>
     * NOTE: This does not delete the table itself. It just deletes the rows of data contained
     * within the table.
     * <p>
     * Potential causes for failure:
     * <p>
     *   1) Within {@link MoviesProvider#delete(Uri, String, String[])}, you didn't call
     *    getContext().getContentResolver().notifyChange(uri, null) after performing a deletion.
     * <p>
     *   2) The cursor returned from the query was null
     * <p>
     *   3) After the attempted deletion, the ContentProvider still provided movies data
     */
    @Test
    public void testDeleteAllRecordsFromProvider() {

        /*
         * Ensure there are records to delete from the database. Due to our setUp method, the
         * database will not have any records in it prior to this method being run.
         */
        testBulkInsert();

        /*
         * TestContentObserver allows us to test movies or not notifyChange was called
         * appropriately. We will use that here to make sure that notifyChange is called when a
         * deletion occurs.
         */
        TestUtilities.TestContentObserver moviesObserver = TestUtilities.getTestContentObserver();

        /*
         * A ContentResolver provides us access to the content model. We can use it to perform
         * deletions and queries at our CONTENT_URI
         */
        ContentResolver contentResolver = mContext.getContentResolver();

        /* Register a content observer to be notified of changes to data at a given URI (movies) */
        contentResolver.registerContentObserver(
                /* URI that we would like to observe changes to */
                MoviesContract.MovieEntry.CONTENT_URI,
                /* Whether or not to notify us if descendants of this URI change */
                true,
                /* The observer to register (that will receive notifyChange callbacks) */
                moviesObserver);

        /* Delete all of the rows of data from the movies table */
        contentResolver.delete(
                MoviesContract.MovieEntry.CONTENT_URI,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null);

        /* Perform a query of the data that we've just deleted. This should be empty. */
        Cursor shouldBeEmptyCursor = contentResolver.query(
                MoviesContract.MovieEntry.CONTENT_URI,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Sort order to return in Cursor */
                null);

        /*
         * If this fails, it's likely you didn't call notifyChange in your delete method from
         * your ContentProvider.
         */
        moviesObserver.waitForNotificationOrFail();

        /*
         * waitForNotificationOrFail is synchronous, so after that call, we are done observing
         * changes to content and should therefore unregister this observer.
         */
        contentResolver.unregisterContentObserver(moviesObserver);

        /* In some cases, the cursor can be null. That's actually a failure case here. */
        String cursorWasNull = "Cursor was null.";
        assertNotNull(cursorWasNull, shouldBeEmptyCursor);

        /* If the count of the cursor is not zero, all records weren't deleted */
        String allRecordsWereNotDeleted =
                "Error: All records were not deleted from movies table during delete";
        assertEquals(allRecordsWereNotDeleted,
                0,
                shouldBeEmptyCursor.getCount());

        /* Always close your cursor */
        shouldBeEmptyCursor.close();
    }

    private void deleteAllRecordsFromMoviesTable() {
        /* Access writable database through WeatherDbHelper */
        MoviesDbHelper helper = new MoviesDbHelper(mContext);
        SQLiteDatabase database = helper.getWritableDatabase();

        /* The delete method deletes all of the desired rows from the table, not the table itself */
        database.delete(MoviesContract.MovieEntry.TABLE_NAME, null, null);
        database.delete("sqlite_sequence", "name=?", new String[]{MoviesContract.MovieEntry.TABLE_NAME});

        /* Always close the database when you're through with it */
        database.close();
    }
}
