package be.hepl.morgan.applicationpistes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import db.airport.models.Luggage;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Button doneButton;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.luggage_listview);
        Intent intent = getIntent();
        setCheckBoxList((ArrayList<Luggage>) intent.getSerializableExtra("luggage"));

        doneButton = findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneLoading();
            }
        });
    }

    /**
     *
     * @param luggageList
     */
    private void setCheckBoxList(ArrayList<Luggage> luggageList) {
        ArrayAdapter<Luggage> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, luggageList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        for (int i = 0; i < adapter.getCount(); i++) {
            String loaded = (adapter.getItem(i)).getLoaded()+"";
            if ("Y".equalsIgnoreCase(loaded)) {
                listView.setItemChecked(i, true);
            }
        }
    }

    private void doneLoading() {
        ArrayAdapter<Luggage> adapter = (ArrayAdapter<Luggage>) listView.getAdapter();
        ArrayList<Luggage> luggageList = new ArrayList<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            Luggage luggage = adapter.getItem(i);
            boolean b = listView.isItemChecked(i);
            luggage.setReceived(b);
            if (b) luggage.setLoaded('Y');
            else luggage.setLoaded('N');
            luggageList.add(luggage);
        }
        setResult(RESULT_OK, (new Intent()).putExtra("lug_loaded", luggageList));
        finish();
    }

}
