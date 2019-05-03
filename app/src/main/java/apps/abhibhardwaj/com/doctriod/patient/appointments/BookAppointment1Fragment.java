package apps.abhibhardwaj.com.doctriod.patient.appointments;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apps.abhibhardwaj.com.doctriod.patient.R;
import apps.abhibhardwaj.com.doctriod.patient.others.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jaredrummler.materialspinner.MaterialSpinner.OnItemSelectedListener;
import dmax.dialog.SpotsDialog;
import java.util.ArrayList;
import java.util.List;

public class BookAppointment1Fragment extends Fragment {

  private CollectionReference allDoctorsRef;
  private CollectionReference docTypesRef;

  private MaterialSpinner spinner;
  private RecyclerView recyclerView;

  private AlertDialog dialog;

   public static BookAppointment1Fragment instance;


   public static BookAppointment1Fragment getInstance() {
     if (instance == null)
     {
       instance = new BookAppointment1Fragment();
     }
     return instance;
   }

  public BookAppointment1Fragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_book_appointment1, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    allDoctorsRef = FirebaseFirestore.getInstance().collection("AllDoctors");

    dialog = new SpotsDialog.Builder().setContext(getActivity()).setCancelable(false).build();

    spinner = view.findViewById(R.id.apt_step1_spinner_location);
    recyclerView = view.findViewById(R.id.apt_step1_recycler_view);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    recyclerView.addItemDecoration(new SpaceItemDecoration(8));
    fetchAllDoctors();

  }

  private void fetchAllDoctors() {
     allDoctorsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
       @Override
       public void onComplete(@NonNull Task<QuerySnapshot> task) {
         if (task.isSuccessful())
         {
           List<String> list = new ArrayList<>();
           list.add("Please choose city");
           for (QueryDocumentSnapshot documentSnapshot:task.getResult())
           {
             list.add(documentSnapshot.getId());
           }
           loadAllDoctors(list);

         }
       }
     }).addOnFailureListener(new OnFailureListener() {
       @Override
       public void onFailure(@NonNull Exception e) {
         Utils.makeToast(getActivity(), e.getMessage());
       }
     });
  }

  private void loadAllDoctors(List<String> list) {
     spinner.setItems(list);
     spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
       @Override
       public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
         if (position > 0)
         {
           loadAvailableDoctorsInCity(item.toString());
         }
         else
         {
           recyclerView.setVisibility(View.GONE);
         }
       }
     });
  }

  private void loadAvailableDoctorsInCity(String cityName) {
     dialog.show();

     //AllDoctors/Chandigarh/DocType/Dental

    Common.city = cityName;

     docTypesRef = FirebaseFirestore.getInstance().collection("AllDoctors").document(cityName).collection("DocType");
     docTypesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
       @Override
       public void onComplete(@NonNull Task<QuerySnapshot> task) {
         List<DocType> docTypeList = new ArrayList<>();
         if (task.isSuccessful())
         {
            for (QueryDocumentSnapshot documentSnapshot: task.getResult())
            {
                DocType docType = documentSnapshot.toObject(DocType.class);
                docType.setId(documentSnapshot.getId());
                docTypeList.add(docType);

            }
            loadRecyclerView(docTypeList);
         }
       }
     }).addOnFailureListener(new OnFailureListener() {
       @Override
       public void onFailure(@NonNull Exception e) {
         dialog.dismiss();
          Utils.makeToast(getActivity(), e.getMessage());
       }
     });


  }

  private void loadRecyclerView(List<DocType> docTypeList) {
    DocTypeRecyclerAdapter adapter = new DocTypeRecyclerAdapter(getActivity(), docTypeList);
    recyclerView.setAdapter(adapter);
    recyclerView.setVisibility(View.VISIBLE);
    dialog.dismiss();
  }


}
