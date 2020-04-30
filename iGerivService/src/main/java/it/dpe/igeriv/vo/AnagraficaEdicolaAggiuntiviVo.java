package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.util.NumberUtils;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

@Getter
@Setter
@Entity
@NamedQueries({ @NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_ATTRIBUTI_AGGIUNTIVI_EDICOLA_BY_PK, query = "from AnagraficaEdicolaAggiuntiviVo vo left join fetch vo.immagine1 where vo.codEdicola = :codiceRivenditaWeb") })
@Table(name = "tbl_9112", schema = "")
public class AnagraficaEdicolaAggiuntiviVo extends BaseVo {
	private static final long serialVersionUID = 906554889223229235L;
	@Id
	@Column(name = "crivw9112")
	private Integer codEdicola;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "crivw9112", updatable = false, insertable = false, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo anagraficaEdicolaVo;
	@Column(name = "tipo9112")
	private Integer tipo;
	@Column(name = "stipo9112")
	private Integer sottotipo;
	@Column(name = "posiz9112")
	private Integer posizionamento;
	@Column(name = "attr9112")
	private Integer attributo;
	@Column(name = "vm_ricev9112")
	private Boolean vmPresenzaRicevitoria;
	@Column(name = "vm_staest9112")
	private Boolean vmPresenzaStampaEstera;
	@Column(name = "vm_infor9112")
	private Boolean vmRivenditaInformatizzata;
	@Column(name = "vm_regcassa9112")
	private Boolean vmRegistratoreCassa;
	@Column(name = "stag9112")
	private Integer stagionalita;
	@Column(name = "mp_alim9112")
	private Boolean mpAlimentari;
	@Column(name = "mp_abbig9112")
	private Boolean mpAbbigliamento;
	@Column(name = "mp_arred9112")
	private Boolean mpArredamento;
	@Column(name = "mp_artspor9112")
	private Boolean mpArticoliSportivi;
	@Column(name = "mp_carb9112")
	private Boolean mpCarburanti;
	@Column(name = "mp_cartgio9112")
	private Boolean mpCartolibreriaGiocattoli;
	@Column(name = "mp_eletcons9112")
	private Boolean mpElettronicaConsumo;
	@Column(name = "mp_homent9112")
	private Boolean mpHomeEntertainment;
	@Column(name = "mp_bar9112")
	private Boolean mpProdottiBar;
	@Column(name = "mp_tabac9112")
	private Boolean mpTabacchi;
	@Column(name = "mp_merc9112")
	private Boolean mpMerceria;
	@Column(name = "mp_ortofr9112")
	private Boolean mpOrtofrutta;
	@Column(name = "mp_ferr9112")
	private Boolean mpFerramenta;
	@Column(name = "mp_panif9112")
	private Boolean mpPanificio;
	@Column(name = "mp_prof9112")
	private Boolean mpProfumeria;
	@Column(name = "mp_fior9112")
	private Boolean mpFiorista;
	@Column(name = "mp_mix9112")
	private String mpMix;
	@Column(name = "mp_nes9112")
	private Boolean mpNessuna;
	@Column(name = "pa_aerop9112")
	private Boolean paAeroporto;
	@Column(name = "pa_cencom9112")
	private Boolean paCentroCommerciale;
	@Column(name = "pa_stazfer9112")
	private Boolean paStazioneFerroviaria;
	@Column(name = "pa_porto9112")
	private Boolean paPorto;
	@Column(name = "pa_osped9112")
	private Boolean paOspedale;
	@Column(name = "pa_mezpubb9112")
	private Boolean paFermataMezziPubblici;
	@Column(name = "pa_merriod9112")
	private Boolean paMercatoRionaleDiurno;
	@Column(name = "pa_strfier9112")
	private Boolean paStrutturaFieristica;
	@Column(name = "pa_cendir9112")
	private Boolean paCentroDirezionale;
	@Column(name = "pa_luculto9112")
	private Boolean paLuogoCulto;
	@Column(name = "pa_munic9112")
	private Boolean paMunicipio;
	@Column(name = "pa_poste9112")
	private Boolean paPoste;
	@Column(name = "pa_asl9112")
	private Boolean paAsl;
	@Column(name = "pa_quest9112")
	private Boolean paQuestura;
	@Column(name = "pa_caserma9112")
	private Boolean paCaserma;
	@Column(name = "pa_univer9112")
	private Boolean paUniversita;
	@Column(name = "pa_scmesu9112")
	private Boolean paScuolaMediaSuperiore;
	@Column(name = "pa_scmeinf9112")
	private Boolean paScuolaMediaInferioreElementareAsili;
	@Column(name = "pa_museo9112")
	private Boolean paMuseo;
	@Column(name = "pa_stadio9112")
	private Boolean paStadio;
	@Column(name = "pa_cinema9112")
	private Boolean paCinema;
	@Column(name = "pa_teatro9112")
	private Boolean paTeatro;
	@Column(name = "pa_pales9112")
	private Boolean paPalestra;
	@Column(name = "pa_banca9112")
	private Boolean paBanca;
	@Column(name = "pa_altredit9112")
	private Boolean paAltriPuntiVenditaProdottiEditoriali;
	@Column(name = "pa_gdo9112")
	private Boolean paGdoGda;
	@Column(name = "pa_turisti9112")
	private Boolean paStruttureRicettiveTuristi;
	@Column(name = "pa_nes9112")
	private Boolean paNessuna;
	@Column(name = "localiz9112")
	private Integer localizzazione;
	@Column(name = "datmo9112")
	private Timestamp dataUltimaModifica;
	@Column(name = "mq_super9112")
	private Float mqSuperficie;
	@Column(name = "mq_linea9112")
	private Float mtlineariPerGiornali;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(column = @JoinColumn(name = "crivw9112", insertable = false, updatable = false, referencedColumnName = "crivw9113")),
		@JoinColumnOrFormula(formula = @JoinFormula(value = "1", referencedColumnName = "progf9113"))
	})
	private ImmagineAnagraficaEdicolaVo immagine1;
	@Column(name = "note9112")
	private String note;
	
	public AnagraficaEdicolaAggiuntiviVo() {
		vmPresenzaRicevitoria = false;
		vmPresenzaStampaEstera = false;
		vmRivenditaInformatizzata = false;
		vmRegistratoreCassa = false;
		mpAlimentari = false;
		mpAbbigliamento = false;
		mpArredamento = false;
		mpArticoliSportivi = false;
		mpCarburanti = false;
		mpCartolibreriaGiocattoli = false;
		mpElettronicaConsumo = false;
		mpHomeEntertainment = false;
		mpProdottiBar = false;
		mpTabacchi = false;
		mpMerceria = false;
		mpOrtofrutta = false;
		mpFerramenta = false;
		mpPanificio = false;
		mpProfumeria = false;
		mpFiorista = false;
		mpNessuna = false;
		paAeroporto = false;
		paCentroCommerciale = false;
		paStazioneFerroviaria = false;
		paPorto = false;
		paOspedale = false;
		paFermataMezziPubblici = false;
		paMercatoRionaleDiurno = false;
		paStrutturaFieristica = false;
		paCentroDirezionale = false;
		paLuogoCulto = false;
		paMunicipio = false;
		paPoste = false;
		paAsl = false;
		paQuestura = false;
		paCaserma = false;
		paUniversita = false;
		paScuolaMediaSuperiore = false;
		paScuolaMediaInferioreElementareAsili = false;
		paMuseo = false;
		paStadio = false;
		paCinema = false;
		paTeatro = false;
		paPalestra = false;
		paBanca = false;
		paAltriPuntiVenditaProdottiEditoriali = false;
		paGdoGda = false;
		paStruttureRicettiveTuristi = false;
		paNessuna = false;
		mqSuperficie = null;
		mtlineariPerGiornali = null;
		immagine1 = null;
		note = null;
	}
	
	public String getMqSuperficieFormat() {
		return getMqSuperficie() != null ? NumberUtils.formatNumber(getMqSuperficie()) : null;
	}
	
	public String getMtlineariPerGiornaliFormat() {
		return getMtlineariPerGiornali() != null ? NumberUtils.formatNumber(getMtlineariPerGiornali()) : null;
	}
}
