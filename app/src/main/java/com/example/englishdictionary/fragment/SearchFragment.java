package com.example.englishdictionary.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishdictionary.R;
import com.example.englishdictionary.adapters.DefinitionAdapter;
import com.example.englishdictionary.adapters.PhoneticAdapter;
import com.example.englishdictionary.dictionaryapi.OnFetchDataListener;
import com.example.englishdictionary.dictionaryapi.RequestManager;
import com.example.englishdictionary.dictionaryapi.model.Entry;
import com.example.englishdictionary.dictionaryapi.model.HeadwordEntry;
import com.example.englishdictionary.dictionaryapi.model.LexicalEntry;
import com.example.englishdictionary.dictionaryapi.model.PronunciationsList;
import com.example.englishdictionary.dictionaryapi.model.RetrieveEntry;
import com.example.englishdictionary.dictionarylookup.CategoryEntry;
import com.example.englishdictionary.dictionarylookup.Definition;
import com.example.englishdictionary.dictionarylookup.Phonetic;
import com.example.englishdictionary.dictionarylookup.searchsuggests.DataHelper;
import com.example.englishdictionary.dictionarylookup.searchsuggests.WordSuggest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    AutoCompleteTextView searchView;
    RecyclerView recycler_list;
    RecyclerView recycler_phonetic;
    FloatingActionButton btnSearch;
    DefinitionAdapter definitionAdapter;
    PhoneticAdapter phoneticAdapter;
    MediaPlayer mMediaPlayer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater out = TransitionInflater.from(requireContext());
        setExitTransition(out.inflateTransition(R.transition.fade));

        TransitionInflater in = TransitionInflater.from(requireContext());
        setEnterTransition(in.inflateTransition(R.transition.slide_right));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView = (AutoCompleteTextView) view.findViewById(R.id.search_view);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        recycler_list = (RecyclerView) view.findViewById(R.id.list);
        recycler_phonetic = (RecyclerView) view.findViewById(R.id.phonetic);
        btnSearch = (FloatingActionButton) view.findViewById(R.id.btn_search);

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //show word suggest
                getAllWord();
            }
        });

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    search(searchView.getText().toString());
                    closeKeyboard(view);

                    return true;
                }
                return false;
            }
        });

        RequestManager requestManager = new RequestManager(getContext());
        try {
            requestManager.getWordMeaning(dataListener, "hello");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(searchView.getText().toString());
                closeKeyboard(view);
            }
        });
    }

    private void getAllWord() {
        final List<String> WORDS = new ArrayList<>();
        if (!searchView.getText().toString().equals("")) {
            DataHelper.findSuggestions(getContext(), searchView.getText().toString(), 5
                    , new DataHelper.OnFindWordsListener() {

                @Override
                public void onResults(List<WordSuggest> results) {
                    try {
                        for (WordSuggest w : results) {
                            WORDS.add(w.getWord());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext()
                                , R.layout.search_autocomplete_row, R.id.word_suggest, WORDS);
                        searchView.setAdapter(adapter);
                    } catch (NullPointerException n) {
                        n.printStackTrace();
                    }
                }
            });
        }
    }

    private final OnFetchDataListener dataListener = new OnFetchDataListener() {
        @Override
        public void onFetchData(RetrieveEntry retrieveEntry, String message) {
            if(retrieveEntry == null) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                return;
            }
            showData(retrieveEntry);
        }

        @Override
        public void onError(String message) {
        }
    };

    private void showData(RetrieveEntry retrieveEntry) {
        recycler_phonetic.setHasFixedSize(true);
        recycler_phonetic.setLayoutManager(new LinearLayoutManager(getContext()));

        recycler_list.setHasFixedSize(true);
        recycler_list.setLayoutManager(new LinearLayoutManager(getContext()));

        List<CategoryEntry> categoryEntries = new ArrayList<>();
        List<Definition> definitions = new ArrayList<>();

        List<HeadwordEntry> headwordEntry = retrieveEntry.getResults();
        List<LexicalEntry> lexicalEntries = headwordEntry.get(0).getLexicalEntries();

        Entry entry = lexicalEntries.get(0).getEntries().get(0);
        PronunciationsList pronunciations = entry.getPronunciations();

        List<Phonetic> phonetics = new ArrayList<>();
        try {
            for (int i = 0; i < pronunciations.size(); i++) {
                Phonetic phonetic = new Phonetic(headwordEntry.get(0).getWord()
                        , pronunciations.get(i).getPhoneticSpelling()
                        , pronunciations.get(i).getAudioFile());
                phonetics.add(phonetic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //show phonetics
        mMediaPlayer = new MediaPlayer();
        phoneticAdapter = new PhoneticAdapter(getContext(), phonetics, mMediaPlayer);
        recycler_phonetic.setAdapter(phoneticAdapter);
        try {
            for (LexicalEntry l : lexicalEntries) {
                CategoryEntry categotyEntry = new CategoryEntry(l.getText()
                        , l.getLexicalCategory().getText()
                        , l.getEntries().get(0));
                categoryEntries.add(categotyEntry);
            }

            for (CategoryEntry c : categoryEntries) {
                Definition definition = new Definition(c.getCategory()
                        , c.getWord(), c.getEntry()
                        , c.getEntry().getSenses().get(0));
                definitions.add(definition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //show meanings
        definitionAdapter = new DefinitionAdapter(getContext(), definitions);
        recycler_list.setAdapter(definitionAdapter);
    }

    private void search(String keyword) {
        RequestManager requestManager = new RequestManager(getContext());
        requestManager.getWordMeaning(dataListener, keyword);

        if (mMediaPlayer != null)
            mMediaPlayer.release();
    }

    public void closeKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onStop() {
        if (mMediaPlayer != null)
            mMediaPlayer.release();

        super.onStop();
    }
}