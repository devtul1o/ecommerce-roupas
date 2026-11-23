package br.com.lojaroupas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import br.com.lojaroupas.model.ItemCarrinho;
import br.com.lojaroupas.model.id.ItemCarrinhoId;

public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinho, ItemCarrinhoId>{
    List<ItemCarrinho> findByCarrinhoCodCarrinho(Long codCarrinho);
}
