package com.example.list_app.data.utils;


import com.example.list_app.data.network.Message;

public final class NetworkUtils {

    public static final int UNEXPECTED_ERROR = 9000;

    public static final int CONNECTION_FAILED = 9001;

    public static final int UNAUTHORIZED = 401;

    public static final int FORBIDDEN = 403;

    public static final int NOT_FOUND = 404;

    public static final int METHOD_NOT_ALLOWED = 405;

    public static final int REQUEST_TIMEOUT = 408;

    public static final int INTERNAL_SERVER_ERROR = 500;

    public static final int NOT_IMPLEMENTED = 501;

    public static final int SERVER_UNAVALIABLE = 503;

    public static Message getErrorMessageByCode(int code){
        switch (code){
            case UNAUTHORIZED:
                return Message.make(
                        "Falha na autenticação",
                        "Não foi possivel autenticar com o servidor. Verifique com o administrador se o dispositivo foi cadastrado."
                );
            case FORBIDDEN:
                return Message.make(
                        "Falha na autenticação",
                        "O dispositivo não possui autorização para acessar o servidor. Solicite o cadastro ao administrador."
                );
            case NOT_FOUND:
                return Message.make("O recurso solicitado não foi encontrado ou não está disponível. Tente novamente mais tarde.");
            case METHOD_NOT_ALLOWED:
                return Message.make("O método solicitado não é compatível com a requisição.");
            case REQUEST_TIMEOUT:
                return Message.make(
                        "Falha na conexão",
                        "O tempo de resposta foi excedido. Verifique se o endereço do servidor está correto e se possui conexão com a rede."
                );
            case INTERNAL_SERVER_ERROR:
                return Message.make("Falha no servidor", "Ocorreu um erro interno no servidor.");
            case NOT_IMPLEMENTED:
                return Message.make("Falha no servidor", "O servidor não possui ou não suporta o método requerido.");
            case SERVER_UNAVALIABLE:
                return Message.make("O servidor está indisponível. Tente novamente mais tarde.");
            case CONNECTION_FAILED:
                return Message.make(
                        "Falha na conexão",
                        "Ocorreu um erro na conexão com o servidor. Verifique se o endereço e porta de conexão estão corretos."
                );
            case UNEXPECTED_ERROR:
            default:
                return Message.make(
                        "Falha desconhecida",
                        "Ocorreu um erro inesperado ao tentar comunicação com o servidor. Contate o administrador."
                );
        }
    }
}
