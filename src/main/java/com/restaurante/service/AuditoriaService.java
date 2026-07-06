package com.restaurante.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService {

    private static final Logger logger = LoggerFactory.getLogger(AuditoriaService.class);
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDITORIA");

    public void registrarLogin(String username, boolean exitoso, String ipAddress) {
        MDC.put("usuario", username);
        
        if (exitoso) {
            logger.info("Login exitoso para usuario: {} desde IP: {}", username, ipAddress);
            auditLogger.info("LOGIN_EXITOSO | IP: {} | Timestamp: {}", ipAddress, 
                java.time.LocalDateTime.now());
        } else {
            logger.warn("Login fallido para usuario: {} desde IP: {}", username, ipAddress);
            auditLogger.warn("LOGIN_FALLIDO | IP: {} | Timestamp: {}", ipAddress, 
                java.time.LocalDateTime.now());
        }
        
        MDC.clear();
    }

    public void registrarVenta(Long ventaId, String cliente, double total, String usuario) {
        MDC.put("usuario", usuario);
        logger.info("Venta registrada: ID={}, Cliente={}, Total=S/ {}", ventaId, cliente, total);
        auditLogger.info("VENTA_CREADA | VentaID: {} | Cliente: {} | Total: S/ {}", 
            ventaId, cliente, total);
        MDC.clear();
    }

    public void registrarGeneracionRecibo(Long ventaId, String cliente, String usuario) {
        MDC.put("usuario", usuario);
        logger.info("Recibo PDF generado para venta: {} - Cliente: {}", ventaId, cliente);
        auditLogger.info("RECIBO_GENERADO | VentaID: {} | Cliente: {}", ventaId, cliente);
        MDC.clear();
    }

    public void registrarEnvioEmail(String destinatario, String asunto, boolean exitoso, String usuario) {
        MDC.put("usuario", usuario);
        if (exitoso) {
            logger.info("Email enviado a: {} - Asunto: {}", destinatario, asunto);
            auditLogger.info("EMAIL_ENVIADO | Destinatario: {} | Asunto: {}", destinatario, asunto);
        } else {
            logger.error("Fallo envío de email a: {} - Asunto: {}", destinatario, asunto);
            auditLogger.error("EMAIL_FALLIDO | Destinatario: {} | Asunto: {}", destinatario, asunto);
        }
        MDC.clear();
    }

    public void registrarOperacionCRUD(String entidad, String operacion, Long id, String usuario) {
        MDC.put("usuario", usuario);
        logger.info("CRUD: {} {} en {} por usuario {}", operacion, id, entidad, usuario);
        auditLogger.info("CRUD | Entidad: {} | Operacion: {} | ID: {} | Usuario: {}", 
            entidad, operacion, id, usuario);
        MDC.clear();
    }
}