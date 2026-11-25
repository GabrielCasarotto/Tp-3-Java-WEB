package br.com.infnet.missioncontrol.service;

import br.com.infnet.missioncontrol.model.Astronauta;
import br.com.infnet.missioncontrol.model.StatusAstronauta;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AstronautaService {
    private ArrayList<Astronauta> astronautas = new ArrayList<>();
    private Long proximoId = 4L;

    public AstronautaService() {
        astronautas.add(new Astronauta(1L, "Ana", "Brasil", 34, 8, StatusAstronauta.DISPONIVEL));
        astronautas.add(new Astronauta(2L, "Carlos", "Brasil", 40, 15, StatusAstronauta.EM_MISSAO));
        astronautas.add(new Astronauta(3L, "Pedro", "Portugal", 28, 2, StatusAstronauta.AFASTADO));
    }

    public List<Astronauta> listarTodos() {
        return astronautas;
    }

    public List<Astronauta> filtrarPorStatus(StatusAstronauta status) {
        List<Astronauta> lista = new ArrayList<>();
        for(Astronauta a : astronautas) {
            if(a.getStatus() == status) {
                lista.add(a);
            }
        }
        return lista;
    }

    public Astronauta buscarPeloId(Long id) {
        for(Astronauta a : astronautas) {
            if(a.getId().equals(id)) {
                return a;
            }
        }
        return null;
    }

    public void adicionar(Astronauta astronauta) {
        validar(astronauta);
        if(existeNome(astronauta.getNome())) {
            throw new IllegalArgumentException("Ja existe um astronauta com esse nome");
        }
        astronauta.setId(proximoId);
        proximoId++;
        astronautas.add(astronauta);
    }

    public void atualizar(Astronauta existente, Astronauta novo) {
        validar(novo);
        // so verifica o nome se ele mudou, senao ia dar erro com ele mesmo
        if(!existente.getNome().equalsIgnoreCase(novo.getNome()) && existeNome(novo.getNome())) {
            throw new IllegalArgumentException("Ja existe um astronauta com esse nome");
        }
        existente.setNome(novo.getNome());
        existente.setPais(novo.getPais());
        existente.setIdade(novo.getIdade());
        existente.setAnosExperiencia(novo.getAnosExperiencia());
        existente.setStatus(novo.getStatus());
    }

    public boolean remover(Long id) {
        for(int i = 0; i < astronautas.size(); i++) {
            if(astronautas.get(i).getId().equals(id)) {
                astronautas.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean existeNome(String nome) {
        for(Astronauta a : astronautas) {
            if(a.getNome().equalsIgnoreCase(nome)) {
                return true;
            }
        }
        return false;
    }

    public void validar(Astronauta astronauta) {
        if(astronauta.getNome() == null || astronauta.getNome().trim().equals("")) {
            throw new IllegalArgumentException("Nome é obrigatorio");
        }
        if(astronauta.getPais() == null || astronauta.getPais().trim().equals("")) {
            throw new IllegalArgumentException("Pais é obrigatorio");
        }
        if(astronauta.getIdade() == null) {
            throw new IllegalArgumentException("Idade é obrigatoria");
        }
        if(astronauta.getIdade() < 18) {
            throw new IllegalArgumentException("Idade tem que ser maior ou igual a 18");
        }
        if(astronauta.getAnosExperiencia() == null) {
            throw new IllegalArgumentException("Anos de experiencia é obrigatorio");
        }
        if(astronauta.getAnosExperiencia() < 0) {
            throw new IllegalArgumentException("Anos de experiencia nao pode ser negativo");
        }
        if(astronauta.getStatus() == null) {
            throw new IllegalArgumentException("Status é obrigatorio");
        }
    }
}
