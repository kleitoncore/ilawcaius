package com.br.ilawgestao.domains.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ilawgestao.domains.dto.MotivoResultadoDto;
import com.br.ilawgestao.domains.models.MotivoResultado;
import com.br.ilawgestao.domains.repository.MotivoResultadoRepository;

@Service
public class MotivoResultadoService {
	
	@Autowired
	private MotivoResultadoRepository motivoRepository;
	
	public MotivoResultadoDto cadastrar(MotivoResultadoDto dto) {
		MotivoResultado motivo = MotivoResultado.builder()
				.nome(dto.getNome())
				.empresa(dto.getEmpresa())
				.build();
		return MotivoResultadoDto.build(motivoRepository.save(motivo));
	}
	
	public List<MotivoResultadoDto> listar(long empresa) {
		List<MotivoResultado> motivos = motivoRepository.findByEmpresaCodigoOrderByNomeAsc(empresa);
		List<MotivoResultadoDto> dtos = new ArrayList<MotivoResultadoDto>();
		if(motivos != null && motivos.size() > 0) {
			for(MotivoResultado motivo : motivos) {
				dtos.add(MotivoResultadoDto.build(motivo));
			}
		}
		
		return dtos;
	}
	
	public MotivoResultadoDto consultar(long codigo) {
		return MotivoResultadoDto.build(motivoRepository.findById(codigo).get());
	}
	
	public MotivoResultadoDto alterar(long codigo, MotivoResultadoDto dto) {
		MotivoResultadoDto motivoConsulta = this.consultar(codigo);
		BeanUtils.copyProperties(dto, motivoConsulta,"codigo","empresa");
		MotivoResultado motivo = MotivoResultado.builder()
				.codigo(motivoConsulta.getCodigo())
				.nome(motivoConsulta.getNome())
				.empresa(motivoConsulta.getEmpresa())
				.build();
		
		return MotivoResultadoDto.build(motivoRepository.save(motivo));
	}
	
	public void excluir(long codigo) {
		motivoRepository.deleteById(codigo);
	}
}
