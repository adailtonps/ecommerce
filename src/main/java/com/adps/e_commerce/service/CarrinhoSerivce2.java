package com.adps.e_commerce.service;

import com.adps.e_commerce.domain.ItemCarrinho;
import com.adps.e_commerce.repository.CarrinhoRepository;
import com.adps.e_commerce.repository.ClienteRepository;
import com.adps.e_commerce.repository.ItemCarrinhoRepository;
import com.adps.e_commerce.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarrinhoSerivce2 {
    @Autowired
    private ItemCarrinhoRepository itemCarrinhoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public List<>
}
