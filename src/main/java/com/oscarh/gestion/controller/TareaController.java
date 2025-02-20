package com.oscarh.gestion.controller;

import com.oscarh.gestion.model.EstadoTarea;
import com.oscarh.gestion.model.Proyecto;
import com.oscarh.gestion.model.Tarea;
import com.oscarh.gestion.repository.ProyectoRepository;
import com.oscarh.gestion.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tareas")
public class TareaController {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @GetMapping("/crear")
    public String mostrarFormularioCrear(@RequestParam(value = "proyectoId", required = false) Long proyectoId, Model model) {
        Tarea tarea = new Tarea();
        if (proyectoId != null) {
            Optional<Proyecto> proyectoOpt = proyectoRepository.findById(proyectoId);
            if (proyectoOpt.isPresent()) {
                tarea.setProyecto(proyectoOpt.get());
            } else {
                return "redirect:/proyectos";
            }
        }
        model.addAttribute("tarea", tarea);
        if (tarea.getProyecto() == null) {
            List<Proyecto> proyectos = proyectoRepository.findAll();
            model.addAttribute("proyectos", proyectos);
        }
        model.addAttribute("estados", EstadoTarea.values());
        return "tareas/crear";
    }

    @PostMapping
    public String guardarTarea(@ModelAttribute Tarea tarea, @RequestParam("proyecto.id") Long proyectoId) {
        Optional<Proyecto> proyectoOpt = proyectoRepository.findById(proyectoId);
        if (proyectoOpt.isPresent()) {
            Proyecto proyecto = proyectoOpt.get();
            proyecto.addTarea(tarea);
            proyectoRepository.save(proyecto);
            return "redirect:/proyectos/" + proyecto.getId();
        }
        return "redirect:/proyectos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarTarea(@PathVariable Long id) {
        Optional<Tarea> tareaOpt = tareaRepository.findById(id);
        if (tareaOpt.isPresent()) {
            Tarea tarea = tareaOpt.get();
            Proyecto proyecto = tarea.getProyecto();
            tareaRepository.delete(tarea);
            if (proyecto != null) {
                return "redirect:/proyectos/" + proyecto.getId();
            }
        }
        return "redirect:/proyectos";
    }
    

}
