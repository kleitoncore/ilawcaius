package com.br.ilawgestao.domains.dto;

import com.br.ilawgestao.domains.models.Atividade;
import com.br.ilawgestao.domains.models.CheckListAtividade;
import com.br.ilawgestao.domains.models.ExecucaoCheckList;
import com.br.ilawgestao.domains.models.Usuario;
import com.br.ilawgestao.domains.utils.DatasUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecucaoCheckListDto {
	private long codigo;
	private CheckListAtividade checkList;
	private Atividade atividade;
	private String dataExecucao;
	private Usuario usuario;
	private String status;
	
	public static ExecucaoCheckListDto build(ExecucaoCheckList exec) {
		ExecucaoCheckListDto dto = new ExecucaoCheckListDto();
		dto.setCodigo(exec.getCodigo());
		dto.setCheckList(exec.getCheckList());
		dto.setAtividade(exec.getAtividade());
		dto.setDataExecucao(exec.getDataExecucao());
		dto.setUsuario(exec.getUsuario());
		dto.setStatus(exec.getStatus());
		return dto;
	}
	
	public static ExecucaoCheckListDto buildConsulta(ExecucaoCheckList exec) {
		ExecucaoCheckListDto dto = new ExecucaoCheckListDto();
		dto.setCodigo(exec.getCodigo());
		dto.setCheckList(exec.getCheckList());
		dto.setAtividade(exec.getAtividade());
		if(exec.getDataExecucao() != null) {
			dto.setDataExecucao(DatasUtil.formatarDataTela(exec.getDataExecucao()));
		} else {
			dto.setDataExecucao(null);
		}
		dto.setUsuario(exec.getUsuario());
		dto.setStatus(exec.getStatus());
		return dto;
	}
}
