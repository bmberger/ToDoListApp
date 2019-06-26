package com.example.simpletodo;

// Briana Berger (06/25/2019)

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Declarations
    ArrayList<String> items; // items data in strings (model)
    ArrayAdapter<String> itemsAdapter; // items that moves the model to the view (controller)
    ListView itemsList;

    // returns the file in which the data is stored
    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    // read the items from the file system
    private void readItems() {
        try {
            // create the array using the content in the file
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            // print the error to the console
            e.printStackTrace();
            // just load an empty list
            items = new ArrayList<>();
        }
    }

    // write the items to the filesystem
    private void writeItems() {
        try {
            // save the item list as a line-delimited text file
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            // print the error to the console
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // intializes the list and its architecture
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        itemsList = (ListView) findViewById(R.id.listItems);
        itemsList.setAdapter(itemsAdapter);

        listViewListener();
    }

    // Adds typed input to list view (public as we are calling it; use private when listener)
    public void addItemClick(View v) {
        // adds the user's input to list
        EditText userInputItem = (EditText) findViewById(R.id.userInput);
        String itemText = userInputItem.getText().toString();
        itemsAdapter.add(itemText);

        // resets the text in input to empty
        userInputItem.setText("");

        // store the updated list
        writeItems();

        // notifies user of what action happened
        Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_SHORT).show();
    }

    // Listens for when someone clicks on an item to remove it
    private void listViewListener() {
        Log.i("MainActivity", "Setting up listener on list view");
        itemsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                // remove the item at position in the items ArrayList<String>
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                Log.i("MainActivity", "Item removed from list at " + position);
                return true;
            }
        });
    }
}
