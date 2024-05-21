package br.alura.TabelaFipe.service;

import java.util.List;

public interface IBuscaDados {
    <T> T consultarDados(String json, Class<T> classe);

    <T> List<T> obterLista(String json, Class<T> classe);
}
