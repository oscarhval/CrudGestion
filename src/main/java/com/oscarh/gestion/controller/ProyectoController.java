package com.oscarh.gestion.controller;

import com.oscarh.gestion.model.EstadoProyecto;
import com.oscarh.gestion.model.Proyecto;
import com.oscarh.gestion.repository.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/proyectos")
public class ProyectoController {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @GetMapping
    public String listarProyectos(Model model) {
        List<Proyecto> proyectos = proyectoRepository.findAll();
        model.addAttribute("proyectos", proyectos);
        return "proyectos/listar";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        model.addAttribute("estados", EstadoProyecto.values());
        return "proyectos/crear";
    }

    @PostMapping
    public String guardarProyecto(@ModelAttribute Proyecto proyecto) {
        proyectoRepository.save(proyecto);
        return "redirect:/proyectos";
    }

    @GetMapping("/{id}")
    public String verDetalles(@PathVariable Long id, Model model) {
        Optional<Proyecto> proyectoOpt = proyectoRepository.findById(id);
        if (proyectoOpt.isPresent()) {
            Proyecto proyecto = proyectoOpt.get();
            proyecto.getTareas().size(); 
            model.addAttribute("proyecto", proyecto);
            return "proyectos/detalle";
        }
        return "redirect:/proyectos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProyecto(@PathVariable Long id) {
        proyectoRepository.deleteById(id);
        return "redirect:/proyectos";
    }
    

}
