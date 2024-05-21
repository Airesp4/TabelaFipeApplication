package br.alura.TabelaFipe.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.alura.TabelaFipe.model.DadosModelo;
import br.alura.TabelaFipe.model.Modelo;
import br.alura.TabelaFipe.model.Veiculo;
import br.alura.TabelaFipe.service.BuscaDados;
import br.alura.TabelaFipe.service.ConsultaApi;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsultaApi consulta = new ConsultaApi();
    private BuscaDados buscador = new BuscaDados();

    private final String ENDERECO = "https://parallelum.com.br/fipe/api/v1";

    public void iniciaConsulta(){
        System.out.println("**********OPÇÕES**********");
        System.out.println("Carro\nMoto\nCaminhão");
        System.out.println("\nDigite uma das opções para consultar valores: ");
        var opcao = teclado.nextLine();
        String endereco = "";
        String json = "";

        if (opcao.toLowerCase().contains("carr")) {
            endereco = ENDERECO + "/carros/marcas";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = ENDERECO + "/motos/marcas";
        } else {
            endereco = ENDERECO + "/caminhoes/marcas";
        }
        json = consulta.consultarDados(endereco);
        
        var marcas = buscador.obterLista(json, Modelo.class);
        marcas.stream()
                .sorted(Comparator.comparing(Modelo::codigo))
                .forEach(System.out::println);

        System.out.println("Informe o código da marca para consulta: ");
        var codigoMarca = teclado.nextLine();

        endereco = endereco + "/" + codigoMarca + "/modelos";
        json = consulta.consultarDados(endereco);
        var modeloLista = buscador.consultarDados(json, DadosModelo.class);

        System.out.println("\nModelos dessa marca: \n");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Modelo::codigo))
                .forEach(System.out::println);
        
        System.out.println("Digite um trecho do nome do automóvel a ser buscado: ");
        var nomeVeiculo = teclado.nextLine();

        List<Modelo> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                        .collect(Collectors.toList());
        System.out.println("\nModelos Filtrados: \n");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite por favor o código do modelo para buscar os valores de avaliação: ");
        var codigoModelo = teclado.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consulta.consultarDados(endereco);
        List<Modelo> anos = buscador.obterLista(json, Modelo.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consulta.consultarDados(enderecoAnos);
            Veiculo veiculo = buscador.consultarDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }
        System.out.println("\nTodos os veículos filtrados com avaliações por ano: \n");
        veiculos.forEach(System.out::println);
    }
}
