package br.com.guismeiram.imoveis.controller;

import br.com.guismeiram.imoveis.models.Imovel;
import br.com.guismeiram.imoveis.service.ImovelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ImovelController {

    private final ImovelService imovelService;

    @GetMapping("/")
    public ModelAndView index(){
        var list = imovelService.findAll();
        var mv = new ModelAndView("index");
        mv.addObject("list",list);

        return mv;
    }


    @GetMapping("/{buscar}")
    public ModelAndView indexGet(@RequestParam(required = false) Long id) {
        ModelAndView mv = new ModelAndView("index");

        if (id != null) {
            Optional<Imovel> optImovel = imovelService.findById(id);
            mv.addObject("list", optImovel.map(List::of).orElseGet(Collections::emptyList));
        } else {
            mv.addObject("list", imovelService.findAll());
        }

        return mv;

    }

    @GetMapping("/cadastrar")
    public ModelAndView getCadastrar(){
        var mv = new ModelAndView("cadastro");
        mv.addObject("cadastro", new Imovel());
        return mv;
    }

    @PostMapping("/cadastrar")
    public ModelAndView salvarImovel(@Valid @ModelAttribute("cadastro") Imovel imovel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("cadastro");
            mv.addObject("cadastro", imovel);
            return mv;
        }

        imovelService.save(imovel);

        // Redireciona para a tela principal após salvar
        return new ModelAndView("redirect:/buscar");
    }

    @GetMapping("/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable Long id) {
        Imovel imovel = imovelService.findById(id).orElse(null);

        if (imovel == null) {
            return new ModelAndView("redirect:/buscar");
        }

        return new ModelAndView("update")
                .addObject("imovel", imovel);
    }

    @PostMapping("/update")
    public ModelAndView updateImovel(
            @Valid Imovel imovel,  // Anotação @Valid para ativar validação
            BindingResult bindingResult) {  // BindingResult DEVE vir imediatamente após o objeto validado

        if (bindingResult.hasErrors()) {
            // Se houver erros, retorne para o formulário de edição
            return new ModelAndView("update")
                    .addObject("imovel", imovel);
        }

        // Só salva se não houver erros
        imovelService.save(imovel);
        return new ModelAndView("redirect:/buscar");
    }


    @GetMapping("/delete/{id}")
    public ModelAndView deleteImovel(@PathVariable("id") long id) {
        Imovel imovel = imovelService.findById(id).orElse(null);

        if (imovel != null) {
            imovelService.delete(imovel);
        }

        // Após a exclusão, volta para a tela principal com a lista atualizada
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("successMessage", "Imóvel deletado com sucesso!");
        mv.addObject("list", imovelService.findAll());
        return mv;
    }

}
