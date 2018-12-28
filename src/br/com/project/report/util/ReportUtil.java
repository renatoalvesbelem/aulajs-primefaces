package br.com.project.report.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;

@Component
public class ReportUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String UNDERLINE = "_";
	private static final String FOLDER_RELATORIOS = "/relatorios";
	private static final String SUBREPORT_DIR = "SUBREPORT_DIR";
	private static final String EXTENSION_ODS = "ods";
	private static final String EXTENSION_XLS = "xls";
	private static final String EXTENSION_HTML = "html";
	private static final String EXTENSION_PDF = "pdf";
	private static String SEPARATOR = File.separator;
	private static final int RELATORIO_PDF = 1;
	private static final int RELATORIO_EXCEL = 2;
	private static final int RELATORIO_HTML = 3;
	private static final int RELATORIO_PLANILHA_OPEN_OFFICE = 4;
	private static final String PONTO = ".";
	private StreamedContent arquivoRetorno = null;
	private String caminhoArquivoRelatorio = "";
	private JRExporter tipoArquivoExportado = null;
	private String extensaoArquivoExportado = "";
	private File arquivoGerado = null;
	private String caminhoSubReport_dir = "";

	public StreamedContent geraRelatorio(List<?> listDataBeanCollectionReport, HashMap parametrosRelatorios,
			String nomeRelatorioJasper, String nomeRelatorioSaida, int tipoRelatorio) throws Exception {
		// Cria a lista de collectionDataSource de beans que carregam os dados do
		// relatório
		JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(
				listDataBeanCollectionReport);

		// Fornecce o caminho físico até a pasta onde contém o relatório compilador
		// .jasper
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.responseComplete();
		ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
		String caminhoRelatorio = servletContext.getRealPath(FOLDER_RELATORIOS);

		File file = new File(
				caminhoRelatorio.concat(SEPARATOR).concat(nomeRelatorioJasper).concat(PONTO).concat("jasper"));

		if (caminhoRelatorio == null || (caminhoRelatorio != null && caminhoRelatorio.isEmpty()) || file.exists()) {
			caminhoRelatorio = this.getClass().getResource(FOLDER_RELATORIOS).getPath();
			SEPARATOR = "";
		}

		// Caminho para imagens
		parametrosRelatorios.put("REPORT_PARAMETERS_IMG", caminhoRelatorio);

		// Caminho completo até o relatório compilado indicado

		String caminhoArquivoJasper = caminhoRelatorio.concat(SEPARATOR).concat(nomeRelatorioJasper).concat(PONTO)
				.concat("jasper");

		// Faz o carregamento do relatório indicado
		JasperReport relatorioJasper = (JasperReport) JRLoader.loadObjectFromFile(caminhoArquivoJasper);

		// Seta paranetros SUBREPORT_DIR como caminho físico para sub-reports
		caminhoSubReport_dir = caminhoRelatorio.concat(SEPARATOR);
		parametrosRelatorios.put(SUBREPORT_DIR, caminhoSubReport_dir);

		// Carregar o arquivo compilado para a memória

		JasperPrint impressoraJasper = JasperFillManager.fillReport(relatorioJasper, parametrosRelatorios,
				jrBeanCollectionDataSource);

		switch (tipoRelatorio) {
		case RELATORIO_PLANILHA_OPEN_OFFICE:
			tipoArquivoExportado = new JROdtExporter();
			extensaoArquivoExportado = EXTENSION_ODS;
			break;
		case RELATORIO_HTML:
			tipoArquivoExportado = new JRHtmlExporter();
			extensaoArquivoExportado = EXTENSION_HTML;
			break;
		case RELATORIO_EXCEL:
			tipoArquivoExportado = new JRXlsExporter();
			extensaoArquivoExportado = EXTENSION_XLS;
			break;
		default:
			tipoArquivoExportado = new JRPdfExporter();
			extensaoArquivoExportado = EXTENSION_PDF;
			break;
		}
		nomeRelatorioSaida += UNDERLINE + DateUtils.getDateAtualReportName();

		// Caminho relatorio exportado
		caminhoArquivoRelatorio.concat(SEPARATOR).concat(nomeRelatorioSaida).concat(PONTO)
				.concat(extensaoArquivoExportado);

		// Criar novo file exportado
		arquivoGerado = new File(caminhoArquivoRelatorio);

		// Preparar Impressão

		tipoArquivoExportado.setParameter(JRExporterParameter.JASPER_PRINT, impressoraJasper);

		// nome arquvivo físico a ser exportado//impresso

		tipoArquivoExportado.setParameter(JRExporterParameter.OUTPUT_FILE, arquivoGerado);

		// Executar Exportação
		tipoArquivoExportado.exportReport();

		arquivoGerado.deleteOnExit();

		// Criar o inputStream para ser usado pelo primefaces

		FileInputStream conteudoRelatorio = new FileInputStream(arquivoGerado);

		// Faz retorno para aplicação

		arquivoRetorno = new DefaultStreamedContent(conteudoRelatorio, "application/".concat(extensaoArquivoExportado),
				nomeRelatorioSaida.concat(PONTO).concat(extensaoArquivoExportado));
		return arquivoRetorno;
	}

}
