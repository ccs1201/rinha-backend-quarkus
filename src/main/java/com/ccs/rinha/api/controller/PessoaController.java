package com.ccs.rinha.api.controller;

import com.ccs.rinha.api.domain.entity.Pessoa;
import com.ccs.rinha.api.model.PessaoInput;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.ResponseStatus;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/")
@RunOnVirtualThread
@Consumes(MediaType.APPLICATION_JSON)
public class PessoaController {

    private static final String PATH = "pessoas/";

    @POST
    @Path(PATH)
    @ResponseStatus(201)
    @Transactional
    public Response create(@Valid PessaoInput input) {

        try {
            var p = input.toPessoa();
            p.persistAndFlush();

            return Response.created(URI.create(PATH.concat(p.getId().toString()))).build();
        } catch (Exception e) {
            return Response.status(422).build();
        }
    }

    @GET
    @Path(PATH + "{id}")
    @ResponseStatus(200)
    public Pessoa findById(@NotNull UUID id) {

        return (Pessoa) Pessoa.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException(NOT_FOUND));
    }

    @GET
    @Path(PATH)
    @ResponseStatus(200)
    public List<Pessoa> findByTermo(@Nullable @QueryParam("t") String t) {
        if (Objects.isNull(t)) {
            throw new WebApplicationException(BAD_REQUEST);
        }

        return Pessoa.findByTermo(t);
    }

    @GET
    @Path("contagem-pessoas")
    @ResponseStatus(200)
    public Long contarPessoas() {
        return Pessoa.count();
    }
}
