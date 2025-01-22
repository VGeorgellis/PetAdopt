package gr.hua.dit.dis.ergasia.service;

import gr.hua.dit.dis.ergasia.entities.Appointment;
import gr.hua.dit.dis.ergasia.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public void save(Appointment appointment) {
        appointmentRepository.save(appointment);
    }
}
