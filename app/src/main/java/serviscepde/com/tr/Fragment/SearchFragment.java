package serviscepde.com.tr.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serviscepde.com.tr.Adapter.KategorIlanAdapter;
import serviscepde.com.tr.App;
import serviscepde.com.tr.Dialogs.DialogOrder;
import serviscepde.com.tr.MainActivity;
import serviscepde.com.tr.Models.IlanKategori.IlanKategoriResponse;
import serviscepde.com.tr.Models.IlanKategori.IlanKategoriResponseDetail;
import serviscepde.com.tr.Models.IlanOzetBilgi;
import serviscepde.com.tr.Models.Response.BaseResponse;
import serviscepde.com.tr.Models.Response.ResponseDetail;
import serviscepde.com.tr.R;
import serviscepde.com.tr.Utils.Utils;


public class SearchFragment extends Fragment {
    
    View generalView;

    private EditText edtArama;
    private TextView txtAra;
    private ImageView imgAra;
    private LinearLayout linFilter,linSirala;
    public static RecyclerView rvAramaIlanlar;

    private static String userToken , searchText , textFromHome , textFromEdit;
    private static String tmp;
    private Context ctx;
    private ArrayList<IlanOzetBilgi> withoutTextList = new ArrayList<>();
    private ArrayList<IlanOzetBilgi> withTextList = new ArrayList<>();

    private FiltreFragment filtreFragment;
    private String [] multipleSearch = {"Baslik" , "ilanAciklamasi"};
    private KategorIlanAdapter withTextAdapter, withoutTextAdapter;
    private static String orderBY = null;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.search_fragment, container, false);

        MainActivity.bottomNav.setVisibility(View.VISIBLE);
        MainActivity.relHeader.setVisibility(View.GONE);

        generalView = rootView;
        ctx = generalView.getContext();

        edtArama = generalView.findViewById(R.id.edtArama);
        txtAra = generalView.findViewById(R.id.txtAra);
        imgAra = generalView.findViewById(R.id.imgAra);
        linFilter = generalView.findViewById(R.id.linFilter);
        linSirala = generalView.findViewById(R.id.linSirala);
        rvAramaIlanlar = generalView.findViewById(R.id.rvAramaIlanlar);

        filtreFragment = new FiltreFragment();


        SharedPreferences sharedPref = ctx.getSharedPreferences("prefs" , Context.MODE_PRIVATE);
        userToken = sharedPref.getString("userToken" , "0");
        Log.i("userToken" ,userToken);

        clearAdapter();
        Bundle search = this.getArguments();


        if(search != null)
        {
            if(search.getString("SearchText") != null)
            {
                tmp = search.getString("SearchText");
                textFromHome = tmp;
                Log.i("GelenText" , textFromHome);

                if(textFromHome.isEmpty())
                {
                    Log.i("SearchTextGelmedi" , textFromHome);
                }
                if(!textFromHome.isEmpty())
                {
                    Log.i("SearchTextGeldi" , textFromHome);
                }
            }


        }

        if(textFromHome != null)
        {
            if(textFromHome.equals("noValue"))
            {

            }
            else{
                edtArama.setText(textFromHome);
            }

        }


        if(edtArama.getText().toString().isEmpty())
        {
            searchWithoutText();
        }

        if(!edtArama.getText().toString().isEmpty())
        {
            searchWithText(textFromHome);
        }

        searchText = textFromHome;


        imgAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearAdapter();
                textFromEdit = edtArama.getText().toString();

                if(textFromEdit.isEmpty())
                {
                    searchWithoutText();
                }
                if(!textFromEdit.isEmpty())
                {
                    searchWithText(textFromEdit);
                    searchText = textFromEdit;
                }

            }
        });



        linSirala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DialogOrder dialogOrder = new DialogOrder(MainActivity.act);
                dialogOrder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogOrder.setCanceledOnTouchOutside(false);
                dialogOrder.setCancelable(false);
                dialogOrder.show();

                dialogOrder.findViewById(R.id.txtFiyatArtan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("ClickedOrder" , "txtFiyatArtan");
                        dialogOrder.dismiss();
                        orderBY = "Ucret ASC";
                        clearAdapter();
                        if(searchText != null)
                        {
                            searchWithText(searchText);
                        }
                        if(searchText == null)
                        {
                            searchWithoutText();
                        }


                    }
                });
                dialogOrder.findViewById(R.id.txtFiyatAzalan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("ClickedOrder" , "txtFiyatAzalan");
                        dialogOrder.dismiss();
                        orderBY = "Ucret DESC";
                        clearAdapter();
                        if(searchText != null)
                        {
                            searchWithText(searchText);
                        }
                        if(searchText == null)
                        {
                            searchWithoutText();
                        }

                    }
                });
                dialogOrder.findViewById(R.id.txtTarihEski).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("ClickedOrder" , "txtTarihEski");
                        dialogOrder.dismiss();
                        orderBY = "create_at ASC";
                        clearAdapter();
                        if(searchText != null)
                        {
                            searchWithText(searchText);
                        }
                        if(searchText == null)
                        {
                            searchWithoutText();
                        }

                    }
                });
                dialogOrder.findViewById(R.id.txtTarihYeni).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("ClickedOrder" , "txtTarihYeni");
                        dialogOrder.dismiss();
                        orderBY = "create_at DESC";
                        clearAdapter();
                        if(searchText != null)
                        {
                            searchWithText(searchText);
                        }
                        if(searchText == null)
                        {
                            searchWithoutText();
                        }

                    }
                });
                dialogOrder.findViewById(R.id.txtBaslikAZ).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("ClickedOrder" , "txtBaslikAZ");
                        dialogOrder.dismiss();
                        orderBY = "Baslik ASC";
                        clearAdapter();
                        if(searchText != null)
                        {
                            searchWithText(searchText);
                        }
                        if(searchText == null)
                        {
                            searchWithoutText();
                        }

                    }
                });
                dialogOrder.findViewById(R.id.txtBaslikZA).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("ClickedOrder" , "txtBaslikZA");
                        dialogOrder.dismiss();
                        orderBY = "Baslik DESC";
                        clearAdapter();
                        if(searchText != null)
                        {
                            searchWithText(searchText);
                        }
                        if(searchText == null)
                        {
                            searchWithoutText();
                        }
                    }
                });
            }
        });




        linFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle searchDetails = new Bundle();

                if(searchText != null)
                {
                    searchDetails.putString("searchText" , searchText);
                }
                if(orderBY != null)
                {
                    searchDetails.putString("orderBY" , orderBY);
                }

                filtreFragment.setArguments(searchDetails);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragMain , filtreFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });




        return rootView;
    }

    private void clearAdapter() {

        withTextList.clear();
        withoutTextList.clear();
        if(withTextAdapter != null)
        {
            withTextAdapter.notifyDataSetChanged();
        }
        if(withoutTextAdapter != null)
        {
            withoutTextAdapter.notifyDataSetChanged();
        }
    }


    private void searchWithText(String searchText) {

        HashMap<String , Object> hashMap = new HashMap<>();
        HashMap<String , Object> hashMap1 = new HashMap<>();

        if(orderBY !=null)
        {
            hashMap1.put("OrderBy" , orderBY);
        }

        hashMap1.put("Search" , searchText);
        hashMap1.put("MultipleSearch" , multipleSearch );
        hashMap1.put("start" , 0);

        hashMap.put("param" , hashMap1);


        Call<BaseResponse> call = App.getApiService().ilanFiltrele(hashMap);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                ResponseDetail responseDetail = response.body().getResponseDetail();
                String token = responseDetail.getResult();
                JSONObject ilanlar = Utils.jwtToJsonObject(token);


                try {

                    for(int i = 0; i < ilanlar.getJSONObject("OutPutMessage").getJSONArray("Data").length(); i++)
                    {

                        JSONObject tmp = ilanlar.getJSONObject("OutPutMessage").getJSONArray("Data").getJSONObject(i);

                        String ID = tmp.getString("ID");
                        String ilanCity = tmp.getString("ilanCity");
                        String Baslik = tmp.getString("Baslik");
                        String Ucret = null;
                        String Resimler = null;

                        if(tmp.has("Ucret"))
                        {
                            Ucret = tmp.getString("Ucret");
                        }
                        else
                        {
                            Log.i("Ilan Özet Bilgi" ,  "UcretYok");
                        }

                        if(tmp.has("Resimler"))
                        {
                            Resimler = tmp.getString("Resimler");
                        }
                        else
                        {
                            Log.i("Ilan Özet Bilgi" ,  "ResimYok");
                        }

                        Log.i("Ilan Özet Bilgi" , ID + "\t" + ilanCity + "\t" + Baslik + "\t" + Ucret + "\t" + Resimler);

                        IlanOzetBilgi bilgi = new IlanOzetBilgi(ID,ilanCity,Baslik,Ucret,Resimler);
                        withTextList.add(bilgi);

                    }

                    Log.i("Liste1" , String.valueOf(withTextList.size()));

                    withTextAdapter = new KategorIlanAdapter(R.layout.row_ilan , withTextList ,0);
                    withTextAdapter.notifyDataSetChanged();
                    rvAramaIlanlar.setLayoutManager(new LinearLayoutManager(ctx));
                    rvAramaIlanlar.setItemAnimator(new DefaultItemAnimator());
                    rvAramaIlanlar.setAdapter(withTextAdapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });

    }

    private void searchWithoutText() {

        HashMap<String , Object> hashMap = new HashMap<>();
        HashMap<String , Object> hashMap1 = new HashMap<>();

        if(orderBY != null)
        {
            hashMap1.put("OrderBy" , orderBY);
        }

        hashMap1.put("start" , 0);
        hashMap1.put("limit" , 100);

        hashMap.put("param" , hashMap1);

        Call<IlanKategoriResponse> ilanKategoriResponseCall = App.getApiService().getIlanbyKategori(hashMap);
        ilanKategoriResponseCall.enqueue(new Callback<IlanKategoriResponse>() {
            @Override
            public void onResponse(Call<IlanKategoriResponse> call, Response<IlanKategoriResponse> response) {

                IlanKategoriResponseDetail responseDetail = response.body().getIlanKategoriResponseDetail();
                String token = responseDetail.getResult();
                JSONObject ilanlar = Utils.jwtToJsonObject(token);


                try {

                    for(int i = 0; i < ilanlar.getJSONObject("OutPutMessage").getJSONArray("Data").length(); i++)
                    {

                        JSONObject tmp = ilanlar.getJSONObject("OutPutMessage").getJSONArray("Data").getJSONObject(i);

                        String ID = tmp.getString("ID");
                        String ilanCity = tmp.getString("ilanCity");
                        String Baslik = tmp.getString("Baslik");
                        String Ucret = null;
                        String Resimler = null;

                        if(tmp.has("Ucret"))
                        {
                            Ucret = tmp.getString("Ucret");
                        }
                        else
                        {
                            Log.i("Ilan Özet Bilgi" ,  "UcretYok");
                        }

                        if(tmp.has("Resimler"))
                        {
                            Resimler = tmp.getString("Resimler");
                        }
                        else
                        {
                            Log.i("Ilan Özet Bilgi" ,  "ResimYok");
                        }

                        Log.i("Ilan Özet Bilgi" , ID + "\t" + ilanCity + "\t" + Baslik + "\t" + Ucret + "\t" + Resimler);

                        IlanOzetBilgi bilgi = new IlanOzetBilgi(ID,ilanCity,Baslik,Ucret,Resimler);
                        withoutTextList.add(bilgi);

                    }

                    Log.i("Liste1" , String.valueOf(withoutTextList.size()));

                    withoutTextAdapter = new KategorIlanAdapter(R.layout.row_ilan , withoutTextList ,0);
                    withoutTextAdapter.notifyDataSetChanged();
                    rvAramaIlanlar.setLayoutManager(new LinearLayoutManager(ctx));
                    rvAramaIlanlar.setItemAnimator(new DefaultItemAnimator());
                    rvAramaIlanlar.setAdapter(withoutTextAdapter);




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<IlanKategoriResponse> call, Throwable t) {

            }
        });


    }


}