package com.example.contact_manager_new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contact_manager_new.adapter.ContactAdapter;
import com.example.contact_manager_new.db.DatabaseHelper;
import com.example.contact_manager_new.db.entity.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //variable
    private ContactAdapter contactAdapter;
    public ArrayList<Contact> contactArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Favourite Contacts");

        //recyclerview
        recyclerView = findViewById(R.id.recycler_view_contacts);
        db = new DatabaseHelper(this);

        //contact list


       contactAdapter=new ContactAdapter(this,contactArrayList,MainActivity.this);
        contactArrayList.addAll(db.getAllContacts());

       RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAndEditContacts(false,null,-1);
            }
        });
    }


    public void addAndEditContacts(final boolean isUpdated, final Contact contact, final int position) {

        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.layout_add_contact, null);

        AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertdialogBuilder.setView(view);

        TextView contactTitle = view.findViewById(R.id.new_contact_title);
        final EditText newContact = view.findViewById(R.id.name);
        final EditText contactEmail = view.findViewById(R.id.email);

        contactTitle.setText(!isUpdated ? "Add new Contact" : "Edit Contact");
        if (isUpdated && contact != null) {
            newContact.setText(contact.getName());
            contactEmail.setText(contact.getEmail());

        }
        alertdialogBuilder.setCancelable(false).setPositiveButton(isUpdated ? "Update" : "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isUpdated) {
                    DeleteContact(contact, position);
                } else {
                    dialog.cancel();
                }
            }
        });

        final AlertDialog alertDialog = alertdialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(newContact.getText().toString())) {
                    Toast.makeText(MainActivity.this, "please enter name", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                if (isUpdated && contact != null) {
                    UpdateContact(newContact.getText().toString(), contactEmail.getText().toString(), position);
                } else {
                    CreateContact(newContact.getText().toString(), contactEmail.getText().toString());
                }
            }
        });
    }

    public void CreateContact(String name, String email) {
        long id = db.insertContact(name, email);
        Contact contact = db.getContact(id);

        if (contact != null) {
            contactArrayList.add(0, contact);
            contactAdapter.notifyDataSetChanged();
        }
    }

    public void UpdateContact(String name, String email, int position) {

        Contact contact = contactArrayList.get(position);

        contact.setName(name);
        contact.setEmail(email);

        db.updateContact(contact);
        contactArrayList.set(position, contact);
        contactAdapter.notifyDataSetChanged();
    }

    private void DeleteContact(Contact contact, int position) {

        contactArrayList.remove(position);
        db.deleteContact(contact);
        contactAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

