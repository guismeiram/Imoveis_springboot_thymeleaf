package br.com.guismeiram.imoveis.service;

import br.com.guismeiram.imoveis.models.Imovel;
import br.com.guismeiram.imoveis.repository.ImovelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImovelService {

    private final ImovelRepository imovelRepository;

    public List<Imovel> findAll() {
        return imovelRepository.findAll();
    }

    @Transactional
    public Imovel save(Imovel imovel) {
        return imovelRepository.save(imovel);
    }

    public Optional<Imovel> findById(Long id) {
        return imovelRepository.findById(id);
    }

    public void delete(Imovel imovel) {
        imovelRepository.delete(imovel);
    }

    public Imovel updateImovel(Imovel imovel) {
        Optional<Imovel> result = imovelRepository.findById(imovel.getId());
        Imovel existing = new Imovel();
        existing.setBairro(imovel.getBairro());
        existing.setNumero(imovel.getNumero());
        existing.setRua(imovel.getRua());

        return imovelRepository.save(existing);
    }

    
}
