package com.restaurante.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidacionService {

    public void validarNoNulo(Object objeto, String mensaje) {
        Preconditions.checkNotNull(objeto, mensaje);
    }

    public void validarNoVacio(String valor, String campo) {
        if (StringUtils.isBlank(valor)) {
            throw new IllegalArgumentException(campo + " no puede estar vacío");
        }
    }

    public String sanitizarEntrada(String input) {
        if (StringUtils.isBlank(input)) {
            return "";
        }
        return StringUtils.trim(StringUtils.abbreviate(
            StringUtils.replaceEach(input, 
                new String[]{"<", ">", "\"", "'", "&"}, 
                new String[]{"", "", "", "", ""}),
            255));
    }

    public List<String> obtenerCategoriasPlatos() {
        return ImmutableList.of(
            "Entradas",
            "Platos Principales", 
            "Bebidas",
            "Postres",
            "Menú Infantil"
        );
    }

    public void validarRango(int valor, int min, int max, String campo) {
        Preconditions.checkArgument(valor >= min && valor <= max,
            "%s debe estar entre %d y %d", campo, min, max);
    }

    public boolean esEmailValido(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        return StringUtils.contains(email, "@") && 
               StringUtils.contains(email, ".") &&
               email.length() <= 100;
    }
}