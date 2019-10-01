package ar.com.sight.android.comun;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ar.com.sight.android.R;
import ar.com.sight.android.Sight;
import ar.com.sight.android.api.modelos.Usuario;

public class EncabezadoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_TITULO = "Titulo";

    // TODO: Rename and change types of parameters
    private String mParamTitulo;

    public EncabezadoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EncabezadoFragment newInstance(String paramTitulo) {
        EncabezadoFragment fragment = new EncabezadoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_TITULO, paramTitulo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamTitulo = getArguments().getString(ARG_PARAM_TITULO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_encabezado, container, false);
        ((TextView)v.findViewById(R.id.lblTitulo)).setText(mParamTitulo);

        Usuario usuario = Sight.getUsuario(getActivity().getApplication());

        if (usuario != null) {
            ((TextView) v.findViewById(R.id.lblBienvenida)).setText(String.format("Bienvenidx: %s %s", usuario.getNombre(), usuario.getApellido()));
        }

        return v;
    }
}
