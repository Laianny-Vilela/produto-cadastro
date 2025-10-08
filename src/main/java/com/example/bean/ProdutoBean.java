package com.example.bean;

import com.example.dao.ProdutoDAO;
import com.example.model.Produto;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
// import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.Serializable;
import java.util.Set;

@ManagedBean(name = "produtoBean")
@ViewScoped
public class ProdutoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Produto produto = new Produto();
    private final ProdutoDAO dao = new ProdutoDAO();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void limpar() {
        this.produto = new Produto();
    }

    public void gravar() {
        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);

        if (!violations.isEmpty()) {
            FacesContext fc = FacesContext.getCurrentInstance();
            for (ConstraintViolation<Produto> v : violations) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, v.getMessage(), null));
            }
            return;
        }

        try {
            Produto salvo = dao.salvar(produto);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Produto salvo com sucesso. ID: " + salvo.getId(), null));
            this.produto = new Produto();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar: " + e.getMessage(), null));
        }
    }
}
