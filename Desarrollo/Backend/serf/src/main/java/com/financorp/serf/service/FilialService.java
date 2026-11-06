package com.financorp.serf.service;

import com.financorp.serf.entity.Filial;
import com.financorp.serf.repository.FilialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FilialService {

    private final FilialRepository filialRepository;

    public FilialService(FilialRepository filialRepository) {
        this.filialRepository = filialRepository;
    }

    public List<Filial> listarFiliales() {
        return filialRepository.findAll();
    }

    public Filial obtenerFilial(Long id) {
        return filialRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Filial no encontrada: " + id));
    }

    public List<Filial> buscarPorPais(String pais) {
        return filialRepository.findByPais(pais);
    }

    public Filial registrarFilial(Filial filial) {
        if (filialRepository.existsByNombreAndPais(
                filial.getNombre(), filial.getPais())) {
            throw new RuntimeException(
                    "Ya existe una filial con ese nombre en el país indicado.");
        }

        return filialRepository.save(filial);
    }

    public Filial actualizarFilial(Long id, Filial datosFilial) {
        Filial filial = obtenerFilial(id);

        if ((!filial.getNombre().equals(datosFilial.getNombre())
                || !filial.getPais().equals(datosFilial.getPais()))
                && filialRepository.existsByNombreAndPais(
                        datosFilial.getNombre(), datosFilial.getPais())) {
            throw new RuntimeException(
                    "Ya existe una filial con ese nombre en el país indicado.");
        }

        filial.actualizarFilial(
                datosFilial.getNombre(),
                datosFilial.getPais(),
                datosFilial.getCiudad(),
                datosFilial.getDireccion()
        );

        filial.setEstado(datosFilial.getEstado());

        return filialRepository.save(filial);
    }

    public void eliminarFilial(Long id) {
        Filial filial = obtenerFilial(id);
        filialRepository.delete(filial);
    }
}
