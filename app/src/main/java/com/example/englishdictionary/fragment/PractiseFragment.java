package com.example.englishdictionary.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.englishdictionary.MainActivity;
import com.example.englishdictionary.MyApplication;
import com.example.englishdictionary.R;
import com.example.englishdictionary.practise.Card;
import com.example.englishdictionary.practise.Deck;
import com.example.englishdictionary.practise.DeckEditingActivity;
import com.example.englishdictionary.practise.PractiseActivity;
import com.example.englishdictionary.practise.PractiseManager;
import com.example.englishdictionary.practise.WordType;
import com.example.englishdictionary.practise.adapter.DeckAdapter;
import com.example.englishdictionary.practise.file.FileHandler;
import com.example.englishdictionary.practise.file.Meta;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class PractiseFragment extends Fragment {
    private static PractiseManager practiseManager;


    public PractiseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        practiseManager = new PractiseManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_practise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prepareData();

        ListView deckListView = view.findViewById(R.id.listView_deck);
        FloatingActionButton btnAddDeck = view.findViewById(R.id.btn_addDeck);
        practiseManager.getDecks().sort(Deck::compare);
        DeckAdapter deckAdapter = new DeckAdapter(MyApplication.getAppContext(),R.layout.deck_display_practise,practiseManager.getDecks());

        deckListView.setAdapter(deckAdapter);

        deckListView.setOnItemClickListener(((adapterView, view1, i, l) -> {
            Intent intent = new Intent(getActivity(), DeckEditingActivity.class);
            practiseManager.setSelectedDeck(practiseManager.getDecks().get(i));

            startActivityForResult(intent,1);
        }));
        btnAddDeck.setOnClickListener(v->{

        });
        FloatingActionButton btnAddSample = view.findViewById(R.id.btn_addSampleDeck);
        btnAddSample.setOnClickListener(v->{
            generateSample();
            reload();
        });
    }

    private void prepareData(){
        Meta meta;
        meta = FileHandler.LoadMeta(MyApplication.getAppContext());
        if(meta == null){
            meta = new Meta();
        }
        PractiseManager.setMeta(meta);
        List<Deck> decks = new ArrayList<>();
        for (String data: meta.getData()) {
            try{
                decks.add(FileHandler.LoadDeck(MyApplication.getAppContext(),data));
            }catch (Exception e){}
        }
        practiseManager.setDecks(decks);
        if(!decks.isEmpty())
            practiseManager.setSelectedDeck(decks.get(0));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //1 add
        //2 save
        //3 delete
        //4 practise
        switch (resultCode){
            case 2:
                FileHandler.Save(MyApplication.getAppContext(),practiseManager.getSelectedDeck(),practiseManager.getSelectedDeck().getPrefName());
                reload();
                break;
            case 3:
                String name = data.getStringExtra("Deck Pref");
                PractiseManager.getMeta().remove(name);
                FileHandler.Save(MyApplication.getAppContext(),PractiseManager.getMeta(),PractiseManager.getMeta().getPrefName());
                FileHandler.Delete(MyApplication.getAppContext(),name);
                reload();
                break;
            case 4:
                Intent intent = new Intent(getActivity(), PractiseActivity.class);
                startActivityForResult(intent,1);
                break;
        }
    }
    private void reload(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame,new PractiseFragment());
        fragmentTransaction.commit();
    }

    private void generateSample(){
        if(PractiseManager.getMeta() == null){
            PractiseManager.setMeta(new Meta());
        }
        Deck sampleDeck1 = new Deck("Programing vocabulary");
        sampleDeck1.addCard(new Card("Algorithm","Thu???t to??n",WordType.NOUN));
        sampleDeck1.addCard(new Card("Argument","?????i s???",WordType.NOUN));
        sampleDeck1.addCard(new Card("Array","M???ng",WordType.NOUN));
        sampleDeck1.addCard(new Card("Assignment","G??ng gi?? tr???",WordType.NOUN));
        sampleDeck1.addCard(new Card("Brackets","D???u ngo???c",WordType.NOUN));
        sampleDeck1.addCard(new Card("Bug","L???i",WordType.NOUN));
        sampleDeck1.addCard(new Card("Call","ch???y l???nh trong h??m",WordType.VERB));
        sampleDeck1.addCard(new Card("Class","L???p ?????i t?????ng",WordType.NOUN));
        sampleDeck1.addCard(new Card("Comment","Ch?? th??ch",WordType.NOUN));
        sampleDeck1.addCard(new Card("Comment out","Chuy???n l???nh th??nh ch?? th??ch",WordType.VERB));
        sampleDeck1.addCard(new Card("Compiler","Tr??nh bi??n d???ch",WordType.NOUN));
        sampleDeck1.addCard(new Card("Constant","H???ng s???",WordType.NOUN));
        sampleDeck1.addCard(new Card("Crash","D???ng ch????ng tr??nh v?? l???i",WordType.VERB));
        sampleDeck1.addCard(new Card("Data structure","C???u tr??c d??? li???u",WordType.NOUN));
        sampleDeck1.addCard(new Card("Debug","G??? l???i",WordType.VERB));
        sampleDeck1.addCard(new Card("Executable","Ch????ng tr??nh c?? th??? ch???y",WordType.NOUN));
        sampleDeck1.addCard(new Card("Function","H??m",WordType.NOUN));
        sampleDeck1.addCard(new Card("Function call","G???i h??m",WordType.VERB));
        sampleDeck1.addCard(new Card("Implement","hi???n th???c h??a",WordType.VERB));
        sampleDeck1.addCard(new Card("Instance","M???t ?????i t?????ng",WordType.NOUN));
        sampleDeck1.addCard(new Card("Loop","V??ng l???p",WordType.NOUN));
        sampleDeck1.addCard(new Card("Method","Ph????ng th???c",WordType.NOUN));
        sampleDeck1.addCard(new Card("Nested","L???ng v??o nhau",WordType.ADJECTIVE));
        sampleDeck1.addCard(new Card("Object","?????i t?????ng",WordType.NOUN));
        sampleDeck1.addCard(new Card("Object-oriented","H?????ng ?????i t?????ng",WordType.ADJECTIVE));
        sampleDeck1.addCard(new Card("Procedure","Th??? t???c",WordType.NOUN));
        sampleDeck1.addCard(new Card("Read","?????c d??? li???u",WordType.VERB));
        sampleDeck1.addCard(new Card("Return","Tr??? v???",WordType.VERB));
        sampleDeck1.addCard(new Card("Return value","Gi?? tr??? tr??? v???",WordType.NOUN));
        sampleDeck1.addCard(new Card("Run","Ch???y ch????ng tr??nh",WordType.VERB));
        sampleDeck1.addCard(new Card("String","Chu???i",WordType.NOUN));
        sampleDeck1.addCard(new Card("Syntax","C?? ph??p",WordType.NOUN));
        sampleDeck1.addCard(new Card("Type","Ki???u d??? li???u",WordType.NOUN));
        sampleDeck1.addCard(new Card("Value","Gi?? tr???",WordType.NOUN));
        sampleDeck1.addCard(new Card("Variable","Bi???n",WordType.NOUN));
        sampleDeck1.addCard(new Card("Write","Ghi d??? li???u",WordType.NOUN));
        //add to meta


        try{
            Meta meta = PractiseManager.getMeta();
            FileHandler.Save(MyApplication.getAppContext(),sampleDeck1,sampleDeck1.getPrefName());
            meta.addData(sampleDeck1.getPrefName());
            FileHandler.Save(MyApplication.getAppContext(),meta, meta.getPrefName());
        }catch (Exception e){

        }
        //Deck sampleDeck2;
    }
}