
package com.financorp.serf.service;

import com.financorp.serf.entity.Producto;
import com.financorp.serf.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public List<Producto> listarProductosActivos() {
        return productoRepository.findByEstadoTrue();
    }

    public Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Producto no encontrado: " + id));
    }

    public Producto buscarPorCodigo(String codigo) {
        return productoRepository.findByCodigo(codigo)
                .orElseThrow(() ->
                        new RuntimeException("Producto no encontrado con código: " + codigo));
    }

    public List<Producto> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    public Producto registrarProducto(Producto producto) {
        if (productoRepository.existsByCodigo(producto.getCodigo())) {
            throw new RuntimeException("El código del producto ya está registrado.");
        }

        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, Producto datosProducto) {
        Producto producto = obtenerProducto(id);

        if (!producto.getCodigo().equals(datosProducto.getCodigo())
                && productoRepository.existsByCodigo(datosProducto.getCodigo())) {
            throw new RuntimeException("El código del producto ya está registrado.");
        }

        producto.actualizarProducto(
                datosProducto.getNombre(),
                datosProducto.getCategoria(),
                datosProducto.getDescripcion()
        );

        producto.setCodigo(datosProducto.getCodigo());
        producto.setPaisOrigen(datosProducto.getPaisOrigen());
        producto.setPrecioCompra(datosProducto.getPrecioCompra());
        producto.setPrecioVenta(datosProducto.getPrecioVenta());
        producto.setEstado(datosProducto.getEstado());

        return productoRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        Producto producto = obtenerProducto(id);
        productoRepository.delete(producto);
    }
}
