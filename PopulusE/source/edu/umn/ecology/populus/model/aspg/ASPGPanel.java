/*******************************************************************************
 * Copyright (c) 2015 Regents of the University of Minnesota.
 *
 * This software is released under GNU General Public License 2.0
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 *******************************************************************************/
package edu.umn.ecology.populus.model.aspg;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

import edu.umn.ecology.populus.visual.SimpleVFlowLayout;
import edu.umn.ecology.populus.plot.*;
import edu.umn.ecology.populus.visual.*;
import edu.umn.ecology.populus.visual.ppfield.*;
import edu.umn.ecology.populus.visual.stagegraph.*;
import edu.umn.ecology.populus.visual.matrixtable.*;
import edu.umn.ecology.populus.edwin.ModelPanelEventTypes;
import edu.umn.ecology.populus.fileio.Logging;

/**
 * Age-Structured Population Growth
 */
public class ASPGPanel extends BasicPlotInputPanel implements java.io.Externalizable {
	ResourceBundle res = ResourceBundle.getBundle("edu.umn.ecology.populus.model.aspg.Res");
	//birth pattern
	public static final int kCONTINUOUS    = 1;
	public static final int kPREBREEDING   = 2;
	public static final int kPOSTBREEDING  = 3;
	//Plot type
	public static final int kLAMBDAVST     = 4;
	public static final int kSIGMANXVST    = 5;
	public static final int kNXSIGMANXVST  = 6;
	public static final int kVXVSX         = 7;
	public static final int kNXSIGMANXVSX  = 8;
	public static final int kXVSNXSIGMANXT = 9;
	public static final int kXVSNXT        = 10;
	public static final int kTABULAROUTPUT = 11;
	public static final int kLESLIEMATRIX  = 12;
	public static final int kMXVSX         = 13;
	public static final int kLXVSX         = 14;
	public static final int kEIGEN         = 15;
	//input type
	public static final int kLXMX          = 16;
	public static final int kStage         = 17;
	public static final int kProjection    = 18;

	//Pure GUI (stateless)
	private JPanel outputTypePanel = new JPanel();
	private Border border1;
	private TitledBorder titledBorder1;
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel outputParameterPanel = new JPanel();
	private SimpleVFlowLayout simpleVFlowLayout1 = new SimpleVFlowLayout();
	private Border border2;
	private TitledBorder titledBorder2;
	private JTabbedPane inputPane = new JTabbedPane();
	private JScrollPane lxmxScroller = new JScrollPane();
	private JPanel ageClassToViewPanel = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private SimpleVFlowLayout simpleVFlowLayout2 = new SimpleVFlowLayout();
	private JPanel birthPanel = new JPanel();
	private GridLayout gridLayout3 = new GridLayout();
	private JPanel censusTypePanel = new JPanel();
	private Border border4;
	private TitledBorder titledBorder4;
	private Border border6;
	private TitledBorder titledBorder6;
	private JPanel centerPanel = new JPanel();
	private JLabel birthPulseSubLabel = new JLabel();
	private JLabel birthFlowSubLabel = new JLabel();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();
	private JPanel outputTypes2 = new JPanel();
	private JPanel outputTypes1 = new JPanel();
	private GridBagLayout gridBagLayout4 = new GridBagLayout();
	private GridLayout gridLayout1 = new GridLayout();
	private GridLayout gridLayout2 = new GridLayout();
	private JPanel l1P = new JPanel();
	private TitledBorder titledBorder7;
	private GridBagLayout gridBagLayout5 = new GridBagLayout();
	private GridBagLayout gridBagLayout6 = new GridBagLayout();
	private JScrollPane projectionScroller = new JScrollPane();
	private JScrollBar jsbh = new JScrollBar();
	private JScrollBar jsbv = new JScrollBar();
	//Note:  Table will contain
	private JTable projectionTable = new JTable(){
		/**
		 *
		 */
		private static final long serialVersionUID = 6913265565342644634L;

		// Override this method so that it returns the preferred
		// size of the JTable instead of the default fixed size
		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return getPreferredSize();
		}
	};
	private JTable lxmxTable = new JTable();
	private Component comp;

	//Buttons
	private ButtonGroup bgOutputType = new ButtonGroup();
	private StyledRadioButton lambdaVsTButton = new StyledRadioButton();
	private StyledRadioButton sigmaNxVsTButton = new StyledRadioButton();
	private StyledRadioButton nxsnxvstButton = new StyledRadioButton();
	private StyledRadioButton vxvsxButton = new StyledRadioButton();
	private StyledRadioButton tabularOutputButton = new StyledRadioButton();
	private StyledRadioButton xvsnxsnxtButton = new StyledRadioButton();
	private StyledRadioButton nxsnxvsxButton = new StyledRadioButton();
	private StyledRadioButton xvsnxtButton = new StyledRadioButton();
	private StyledRadioButton lxVsXButton = new StyledRadioButton();
	private StyledRadioButton mxVsXButton = new StyledRadioButton();

	private ButtonGroup bgCensusType = new ButtonGroup();
	private StyledRadioButton eigenButton = new StyledRadioButton();
	private StyledRadioButton leslieMatrixButton = new StyledRadioButton();

	private ButtonGroup bgBirthType = new ButtonGroup();
	private JRadioButton birthFlowButton = new JRadioButton();
	private JRadioButton birthPulseButton = new JRadioButton();
	private JRadioButton preBreedingButton = new JRadioButton();
	private JRadioButton postBreedingButton = new JRadioButton();

	private JCheckBox viewAllAgesBox = new JCheckBox();


	//Fields with state
	//Parameters
	private PopulusParameterField numClassesPPF = new PopulusParameterField();
	private PopulusParameterField ageClassToViewPPF = new PopulusParameterField();
	private PopulusParameterField runTimePPF = new PopulusParameterField();
	private PopulusParameterField initialL1PPF = new PopulusParameterField();
	private transient StageStructuredPane sp = new StageStructuredPane(StageStructuredPane.kASPG);
	private int numStages;
	private ASPGData data = new ASPGData();
	private MatrixTableModel projectionModel = new MatrixTableModel(true);

	//Table Fields
	private ASPGLxMxTableModel tableModel = new ASPGLxMxTableModel(4);



	public ASPGPanel() {
		try  {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		sp.startTimer();
	}

	@Override
	public BasicPlot getPlotParamInfo() {

		//MUST MAKE SURE that ageClassTotalField has updated
		numClassesPPF_parameterFieldChanged(null);

		if(lxmxTable.getCellEditor() != null && !lxmxTable.getCellEditor().stopCellEditing()){
			return null;
		}
		if(projectionTable.getCellEditor() != null && !projectionTable.getCellEditor().stopCellEditing()){
			return null;
		}

		int view = viewAllAgesBox.isSelected() ? -1 : ageClassToViewPPF.getInt();
		int mt = getModelType();

		if(mt == kPREBREEDING) view--;
		int ot = getPlotType();
		updateOtherInputs(comp);
		return new ASPGParamInfo(ot, data, view, mapComponent(comp));
	}

	@Override
	public String getOutputGraphName() {
		return res.getString("Age_Structured");
	}

	private void jbInit() throws Exception {
		projectionTable.setModel(projectionModel);
		border1 = BorderFactory.createLineBorder(SystemColor.controlText,1);
		titledBorder1 = new TitledBorder(border1,res.getString("Output_Type"));
		border2 = BorderFactory.createLineBorder(SystemColor.controlText,1);
		titledBorder2 = new TitledBorder(border2,res.getString("Output_Parameters"));
		border4 = BorderFactory.createLineBorder(SystemColor.controlText,1);
		titledBorder4 = new TitledBorder(border4,res.getString("Birth_Pattern"));
		border6 = BorderFactory.createLineBorder(SystemColor.controlText,1);
		titledBorder6 = new TitledBorder(border6,res.getString("Census_Timing"));
		titledBorder7 = new TitledBorder("");
		numClassesPPF.setCurrentValue(3.0);
		numClassesPPF.setDefaultValue(3.0);
		numClassesPPF.setHelpText(res.getString("Total_number_of_age"));
		numClassesPPF.setIntegersOnly(true);
		numClassesPPF.setMaxValue(25.0);
		numClassesPPF.setMinValue(2.0);
		numClassesPPF.setParameterName(res.getString("_i_classes_i_"));
		numClassesPPF.addParameterFieldListener(new edu.umn.ecology.populus.visual.ppfield.ParameterFieldListener() {

			@Override
			public void parameterFieldChanged(ParameterFieldEvent e) {
				numClassesPPF_parameterFieldChanged(e);
			}
		});
		outputTypePanel.setBorder(titledBorder1);
		outputTypePanel.setLayout(gridBagLayout4);

		lambdaVsTButton.setSelected(true);
		lambdaVsTButton.setText("\u03bb vs <i>t</>");
		lambdaVsTButton.setFocusPainted(false);
		lambdaVsTButton.addActionListener(this);
		sigmaNxVsTButton.setText("\u03a3<i>S<sub>x</sub></i> vs <i>t</>");
		sigmaNxVsTButton.setFocusPainted(false);
		sigmaNxVsTButton.addActionListener(this);
		nxsnxvstButton.setText("<i>S<sub>x</sub></i>/\u03a3<i>S<sub>x</sub></i> vs <i>t</>");
		nxsnxvstButton.setFocusPainted(false);
		nxsnxvstButton.addActionListener(this);
		vxvsxButton.setText("<i>V<sub>x</sub></i> vs <i>x</i>");
		vxvsxButton.setFocusPainted(false);
		vxvsxButton.addActionListener(this);
		nxsnxvsxButton.setText("<i>S<sub>x</sub></i>/\u03a3<i>S<sub>x</sub></i> vs <i>x</i>");
		nxsnxvsxButton.setFocusPainted(false);
		nxsnxvsxButton.addActionListener(this);
		xvsnxsnxtButton.setText("<i>x</i>  vs <i>S<sub>x</sub></i>/\u03a3<i>S<sub>x</sub></i> vs <i>t</>");
		xvsnxsnxtButton.setFocusPainted(false);
		xvsnxsnxtButton.addActionListener(this);
		xvsnxtButton.setText("<i>x</i>  vs <i>S<sub>x</sub></i> vs <i>t</>");
		xvsnxtButton.setFocusPainted(false);
		xvsnxtButton.addActionListener(this);
		tabularOutputButton.setText(res.getString("Tabular_Projection"));
		tabularOutputButton.setFocusPainted(false);
		eigenButton.setText("Eigen System");
		eigenButton.setFocusPainted(false);
		lambdaVsTButton.addActionListener(this);
		tabularOutputButton.setLayout(gridBagLayout1);
		outputParameterPanel.setLayout(simpleVFlowLayout1);
		ageClassToViewPPF.setCurrentValue(1.0);
		ageClassToViewPPF.setDefaultValue(1.0);
		ageClassToViewPPF.setEnabled(false);
		ageClassToViewPPF.setHelpText(res.getString("When_graphing_Sx") +
				res.getString("to_view_Acceptable"));
		ageClassToViewPPF.setMaxValue(2.0);
		ageClassToViewPPF.setParameterName(res.getString("_i_Age_Class_To_View"));
		runTimePPF.setCurrentValue(6.0);
		runTimePPF.setDefaultValue(6.0);
		runTimePPF.setHelpText(res.getString("The_number_of_time"));
		runTimePPF.setMaxValue(50.0);
		runTimePPF.setMinValue(1.0);
		runTimePPF.setParameterName(res.getString("_i_Run_Time_i_"));
		outputParameterPanel.setBorder(titledBorder2);
		lxmxTable.setModel(tableModel);
		((DefaultTableCellRenderer)lxmxTable.getDefaultRenderer(Double.class)).setHorizontalAlignment(SwingConstants.CENTER);
		((DefaultTableCellRenderer)lxmxTable.getDefaultRenderer(Integer.class)).setHorizontalAlignment(SwingConstants.CENTER);
		//((DefaultTableCellRenderer)projectionTable.getDefaultRenderer(Double.class)).setHorizontalAlignment(SwingConstants.CENTER);
		projectionTable.setDefaultRenderer(Double.class,new MatrixTableRenderer());
		lxmxTable.setRowSelectionAllowed(false);

		lxmxScroller.setPreferredSize(new Dimension(100, 100));
		this.setLayout(gridBagLayout2);
		viewAllAgesBox.setEnabled(false);
		viewAllAgesBox.setText(res.getString("View_All_Age_Classes"));
		viewAllAgesBox.setFocusPainted(false);
		viewAllAgesBox.addItemListener(new java.awt.event.ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				viewAllAgesBox_itemStateChanged(e);
			}
		});
		ageClassToViewPanel.setLayout(simpleVFlowLayout2);
		simpleVFlowLayout2.setHgap(0);
		birthFlowButton.setText(res.getString("Birth_Flow"));
		birthFlowButton.setFocusPainted(false);
		birthPulseButton.setSelected(true);
		birthPulseButton.setText(res.getString("Birth_Pulse"));
		birthPulseButton.setFocusPainted(false);
		birthPulseButton.addItemListener(new java.awt.event.ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				birthTypeChanged(e);
			}
		});
		birthPanel.setLayout(gridBagLayout3);
		preBreedingButton.setText(res.getString("Prebreeding"));
		preBreedingButton.setFocusPainted(false);
		preBreedingButton.addItemListener(new java.awt.event.ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				preBreedingButton_itemStateChanged(e);
			}
		});
		gridLayout3.setColumns(2);
		postBreedingButton.setSelected(true);
		postBreedingButton.setText(res.getString("Postbreeding"));
		postBreedingButton.setFocusPainted(false);
		censusTypePanel.setLayout(gridLayout3);
		birthPanel.setBorder(titledBorder4);
		censusTypePanel.setBorder(titledBorder6);
		centerPanel.setLayout(gridBagLayout5);
		birthPulseSubLabel.setHorizontalAlignment(SwingConstants.CENTER);
		birthPulseSubLabel.setText(res.getString("_Discrete_"));
		birthFlowSubLabel.setHorizontalAlignment(SwingConstants.CENTER);
		birthFlowSubLabel.setText(res.getString("_Continuous_"));
		lxVsXButton.setText("<i>l<sub>x</sub></i> vs <i>x</i>");
		lxVsXButton.setFocusPainted(false);
		lxVsXButton.addActionListener(this);
		mxVsXButton.setText("<i>m<sub>x</sub></i> vs <i>x</i>");
		mxVsXButton.setFocusPainted(false);
		mxVsXButton.addActionListener(this);
		leslieMatrixButton.setText(res.getString("Leslie_Matrix"));
		leslieMatrixButton.setFocusPainted(false);
		leslieMatrixButton.addActionListener(this);
		outputTypes2.setLayout(gridLayout1);
		gridLayout1.setColumns(2);
		gridLayout1.setRows(5);
		outputTypes1.setLayout(gridLayout2);
		inputPane.addChangeListener(new ASPGPanel_inputPane_changeAdapter(this));
		initialL1PPF.setParameterName("Initial <i>l</i><sub>1</sub> value");
		initialL1PPF.setMaxValue(1.0);
		initialL1PPF.setHelpText(res.getString("The_number_of_time"));
		initialL1PPF.setIncrementAmount(0.1);
		initialL1PPF.setDefaultValue(0.9);
		initialL1PPF.setEnabled(false);
		initialL1PPF.setCurrentValue(0.9);
		l1P.setBorder(titledBorder7);
		l1P.setLayout(gridBagLayout6);
		titledBorder7.setTitle("Prebreeding");
		titledBorder7.setBorder(BorderFactory.createLineBorder(Color.black));
		tableModel.addTableModelListener(new javax.swing.event.TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				tableModel_tableChanged(e);
			}
		});
		jsbh.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				projectionScroller.repaint();
			}
		});
		jsbv.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				projectionScroller.repaint();
			}
		});
		bgOutputType.add(lambdaVsTButton);
		bgOutputType.add(tabularOutputButton);
		bgOutputType.add(sigmaNxVsTButton);
		bgOutputType.add(nxsnxvstButton);
		bgOutputType.add(vxvsxButton);
		bgOutputType.add(nxsnxvsxButton);
		bgOutputType.add(xvsnxsnxtButton);
		bgOutputType.add(xvsnxtButton);
		bgOutputType.add(mxVsXButton);
		bgOutputType.add(lxVsXButton);
		bgOutputType.add(leslieMatrixButton);
		bgOutputType.add(eigenButton);
		bgBirthType.add(birthPulseButton);
		bgBirthType.add(birthFlowButton);
		bgCensusType.add(preBreedingButton);
		bgCensusType.add(postBreedingButton);

		/****************************************************
		 * set up the input types
		 */
		lxmxTable.getColumnModel().getColumn(0).setHeaderRenderer(new TableHeaderRenderer());
		lxmxTable.getColumnModel().getColumn(1).setHeaderRenderer(new TableHeaderRenderer());
		lxmxTable.getColumnModel().getColumn(2).setHeaderRenderer(new TableHeaderRenderer());
		lxmxTable.getColumnModel().getColumn(3).setHeaderRenderer(new TableHeaderRenderer());
		projectionTable.setRowSelectionAllowed(false);
		projectionTable.setTableHeader(null);
		projectionTable.setIntercellSpacing(new Dimension(3,0));
		projectionTable.setBackground(Color.white);
		inputPane.add("Lx Mx Schedule",lxmxScroller);
		data.setPrebreedingParameter(initialL1PPF.getDouble());
		data.setLxMxTable(tableModel.getData(),getModelType(),runTimePPF.getInt());
		sp.numStagesListener(numClassesPPF, ageClassToViewPPF);
		sp.setData(data.getLeslieMatrix(),data.getInitPops(),null,getShift(),getModelType()==kPREBREEDING);
		inputPane.add("Life-Cycle Graph",sp);
		projectionModel.setData(data.getLeslieMatrix(),data.getInitPops(),getShift(),getModelType()==kPREBREEDING);
		inputPane.add("Leslie Matrix",projectionScroller);
		/****************************************************/

		this.add(outputTypePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		outputTypePanel.add(outputTypes1, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		outputTypes1.add(mxVsXButton, null);
		outputTypes1.add(lxVsXButton, null);
		outputTypes2.add(vxvsxButton, null);
		outputTypePanel.add(outputTypes2, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		outputTypes2.add(lambdaVsTButton, null);
		outputTypes2.add(nxsnxvstButton, null);
		outputTypes2.add(sigmaNxVsTButton, null);
		//3D plot button
		outputTypes2.add(xvsnxtButton, null);
		outputTypes2.add(nxsnxvsxButton, null);
		//3D plot button
		outputTypes2.add(xvsnxsnxtButton, null);
		outputTypes2.add(leslieMatrixButton, null);
		outputTypes2.add(tabularOutputButton, null);
		outputTypes2.add(eigenButton, null);

		this.add(outputParameterPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		outputParameterPanel.add(numClassesPPF, null);
		outputParameterPanel.add(runTimePPF, null);
		outputParameterPanel.add(ageClassToViewPanel, null);
		ageClassToViewPanel.add(viewAllAgesBox, null);
		ageClassToViewPanel.add(ageClassToViewPPF, null);
		this.add(inputPane, new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(centerPanel, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		centerPanel.add(birthPanel,   new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		birthPanel.add(birthFlowButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 5, 0));
		birthPanel.add(birthPulseButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 5, 0));
		birthPanel.add(birthFlowSubLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 5, 0));
		birthPanel.add(birthPulseSubLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 5, 0));
		centerPanel.add(censusTypePanel,   new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		censusTypePanel.add(preBreedingButton, null);
		censusTypePanel.add(postBreedingButton, null);
		centerPanel.add(l1P,   new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		l1P.add(initialL1PPF,     new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		lxmxScroller.getViewport().add(lxmxTable, null);
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);


		projectionTable.setShowGrid(false);
		projectionScroller.getViewport().setLayout(gbl);
		projectionScroller.getViewport().setBackground(Color.white);

		jsbh.setOrientation(Adjustable.HORIZONTAL);
		projectionScroller.setHorizontalScrollBar(jsbh);
		projectionScroller.setVerticalScrollBar(jsbv);
		projectionScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		projectionScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		projectionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		projectionTable.setPreferredScrollableViewportSize(inputPane.getSize());
		projectionScroller.getViewport().add(projectionTable,gbc);

		registerChildren(this);
	}

	/*adjusting the minimum width of all the projectiontable elements is a bit of overkill, but
   who cares. bassically, this method has to ensure that the scrollpane is big enough to view the
   matrix*/
	void numClassesPPF_parameterFieldChanged(ParameterFieldEvent e) {
		tableModel.setNumRows(numClassesPPF.getInt()+1);
		projectionModel.setNumRows(numClassesPPF.getInt());

		projectionScroller.getViewport().setExtentSize(new Dimension(1000,1000));
		for(int i=0; i<projectionTable.getColumnCount(); i++){
			projectionTable.getColumnModel().getColumn(i).setMinWidth(40);
			projectionTable.getColumnModel().getColumn(i).setPreferredWidth(50);
		}

		/* this still doesn't handle the size of the matrix perfectly... it seems that when the matrix
      is around 20, it starts to become larger than the viewable area... i'm not sure what's wrong,
      but it's probably not too important*/
		projectionScroller.getViewport().setMinimumSize(new Dimension(1000,1000));

		setAgeClassToView();
	}

	void setAgeClassToView(){
		ageClassToViewPPF.setMaxValue(numClassesPPF.getDouble()+getShift());
		ageClassToViewPPF.setMinValue(1+getShift());
	}

	//need to call super if this is to be used with registerChildren()
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if(nxsnxvstButton.isSelected()){
			this.viewAllAgesBox.setEnabled(true);
			ageClassToViewPPF.setEnabled(!viewAllAgesBox.isSelected());
		} else {
			this.viewAllAgesBox.setEnabled(false);
			ageClassToViewPPF.setEnabled(false);
		}
	}

	void viewAllAgesBox_itemStateChanged(ItemEvent e) {
		ageClassToViewPPF.setEnabled(!viewAllAgesBox.isSelected());
		fireModelPanelEvent(ModelPanelEventTypes.CHANGE_PLOT);
	}

	public int getModelType(){
		int mt;
		if(!birthPulseButton.isSelected()){
			mt = kCONTINUOUS;
		} else {
			mt = preBreedingButton.isSelected() ? kPREBREEDING : kPOSTBREEDING;
		}
		return mt;
	}

	void birthTypeChanged(ItemEvent e) {
		boolean b = birthPulseButton.isSelected();
		preBreedingButton.setEnabled(b);
		postBreedingButton.setEnabled(b);
		modelTypeChanged();
	}

	//if you change this, change the method with the same name in ASPGLxMxTableModel
	int getShift(){
		//      return 0;
		return getModelType() == kPREBREEDING?0:-1;
		//      return getModelType() == kCONTINUOUS?-1:0;
	}

	void preBreedingButton_itemStateChanged(ItemEvent e) {
		setAgeClassToView();
		modelTypeChanged();
	}

	/*if the model type was changed, then we need to make some small adjustments to the tabs*/
	void modelTypeChanged(){
		switch(mapComponent(inputPane.getSelectedComponent())){
		case kLXMX:
			tableModel.setType(getModelType(),true,initialL1PPF.getDouble());
			break;
		case kStage:
			data.setModelType(getModelType());
			sp.setData(data.getLeslieMatrix(),data.getInitPops(),null,getShift(),getModelType()==kPREBREEDING);
			break;
		case kProjection:
			data.setModelType(getModelType());
			projectionModel.setData(data.getLeslieMatrix(),data.getInitPops(),getShift(),getModelType()==kPREBREEDING);
			break;
		}
	}

	/*this method determines which input type is represented by Component c*/
	int mapComponent(Component c){
		if(c instanceof JScrollPane){
			Component[] carray = ((JScrollPane)c).getViewport().getComponents();
			if( carray.length == 0 || ((JTable)carray[0]).getModel() instanceof ASPGLxMxTableModel){
				return kLXMX;
			}
			return kProjection;
		}
		return kStage;
	}

	/*this method ensures that all the tabs have the same data*/
	void updateOtherInputs(Component old){
		data.setPrebreedingParameter(initialL1PPF.getDouble());
		switch(mapComponent(old)){
		case kLXMX:
			data.setLxMxTable(tableModel.getData(),getModelType(),runTimePPF.getInt());

			sp.setData(data.getLeslieMatrix(),data.getInitPops(),null,getShift(),getModelType()==kPREBREEDING);
			projectionModel.setData(data.getLeslieMatrix(),data.getInitPops(),getShift(),getModelType()==kPREBREEDING);
			break;
		case kStage:
			data.setLeslieMatrix(sp.getMatrix(),sp.getPopulations(),getModelType(),runTimePPF.getInt());
			numStages = sp.getPopulations().length;
			numClassesPPF.setCurrentValue(numStages);

			tableModel.setTable(data.getLxMxTable(),data.getModelType());
			projectionModel.setData(data.getLeslieMatrix(),data.getInitPops(),getShift(),getModelType()==kPREBREEDING);
			break;
		case kProjection:
			data.setLeslieMatrix(projectionModel.getMatrix(),projectionModel.getPopulations(),getModelType(),runTimePPF.getInt());

			tableModel.setTable(data.getLxMxTable(),data.getModelType());
			sp.setData(data.getLeslieMatrix(),data.getInitPops(),null,getShift(),getModelType()==kPREBREEDING);
			break;
		}
		projectionScroller.getViewport().setExtentSize(new Dimension(1000,1000));
		for(int i=0; i<projectionTable.getColumnCount(); i++){
			projectionTable.getColumnModel().getColumn(i).setMinWidth(40);
			projectionTable.getColumnModel().getColumn(i).setPreferredWidth(50);
		}
	}

	/*this method is called when the tabbed pane changes tabs*/
	void inputPane_stateChanged(ChangeEvent e) {
		switch(mapComponent(inputPane.getSelectedComponent())){
		case kLXMX:
			numClassesPPF.setEnabled(true);
			initialL1PPF.setEnabled(false);
			break;
		case kStage:
			numClassesPPF.setEnabled(false);
			initialL1PPF.setEnabled(true);
			break;
		case kProjection:
			numClassesPPF.setEnabled(true);
			initialL1PPF.setEnabled(true);
			break;
		}

		if(comp == null){
			comp = inputPane.getSelectedComponent();
			return;
		}
		if(comp.equals(inputPane.getSelectedComponent())) return;
		updateOtherInputs(comp);
		comp = inputPane.getSelectedComponent();
	}

	void tableModel_tableChanged(TableModelEvent e) {
		if(e.getColumn() == 1 && e.getLastRow() == 0 && getModelType() == kPREBREEDING){
			this.initialL1PPF.setCurrentValue(((Double)tableModel.getValueAt(0,1)).doubleValue());
		}
	}

	static class TableHeaderRenderer extends HTMLLabel implements TableCellRenderer {
		/**
		 *
		 */
		private static final long serialVersionUID = 6280875623848463047L;
		public TableHeaderRenderer() {
			super();
		}
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			setBorder(BorderFactory.createRaisedBevelBorder());
			setText((String)value);
			return this;
		}
	}

	private int getPlotType() {
		int ot;

		if (lambdaVsTButton.isSelected()) {
			ot = ASPGPanel.kLAMBDAVST;
		} else if (sigmaNxVsTButton.isSelected()) {
			ot = ASPGPanel.kSIGMANXVST;
		} else if (nxsnxvstButton.isSelected()) {
			ot = ASPGPanel.kNXSIGMANXVST;
		} else if (vxvsxButton.isSelected()) {
			ot = ASPGPanel.kVXVSX;
		} else if (tabularOutputButton.isSelected()) {
			ot = ASPGPanel.kTABULAROUTPUT;
		} else if (xvsnxsnxtButton.isSelected()) { //3d
			ot = ASPGPanel.kXVSNXSIGMANXT;
		} else if (nxsnxvsxButton.isSelected()) {
			ot = ASPGPanel.kNXSIGMANXVSX;
		} else if (xvsnxtButton.isSelected()) {  //3d
			ot = ASPGPanel.kXVSNXT;
		} else if (mxVsXButton.isSelected()) {
			ot = ASPGPanel.kMXVSX;
		} else if (lxVsXButton.isSelected()) {
			ot = ASPGPanel.kLXVSX;
		} else if (leslieMatrixButton.isSelected()) {
			ot = ASPGPanel.kLESLIEMATRIX;
		} else if(eigenButton.isSelected()){
			ot = ASPGPanel.kEIGEN;
		} else{
			//ERROR! no button is selected
			edu.umn.ecology.populus.fileio.Logging.log("ASPGPanel.getPlotParamInfo button is not selected in output type");
			ot = kLAMBDAVST;
		}
		return ot;
	}

	private void setPlotType(int type) {
		if (type == kLAMBDAVST) {
			lambdaVsTButton.setSelected(true);
		} else if (type == kSIGMANXVST) {
			sigmaNxVsTButton.setSelected(true);
		} else if (type == kNXSIGMANXVST) {
			nxsnxvstButton.setSelected(true);
		} else if (type == kVXVSX) {
			vxvsxButton.setSelected(true);
		} else if (type == kTABULAROUTPUT) {
			tabularOutputButton.setSelected(true);
		} else if (type == kXVSNXSIGMANXT) {
			xvsnxsnxtButton.setSelected(true);
		} else if (type == kNXSIGMANXVSX) {
			nxsnxvsxButton.setSelected(true);
		} else if (type == kXVSNXT) {
			xvsnxtButton.setSelected(true);
		} else if (type == kMXVSX) {
			mxVsXButton.setSelected(true);
		} else if (type == kLXVSX) {
			lxVsXButton.setSelected(true);
		} else if (type == kLESLIEMATRIX) {
			leslieMatrixButton.setSelected(true);
		} else if (type == kEIGEN) {
			eigenButton.setSelected(true);
		} else {
			//ERROR! no button is selected
			Logging.log("Unknown type: " + type);
		}

	}


	/**
	 * We will save this panel specially.
	 * There seems to be a problem if we use normal serialization.
	 *
	 * @param out
	 * @throws java.io.IOException
	 */
	@Override
	public void writeExternal(java.io.ObjectOutput out)
			throws java.io.IOException
	{
		out.writeInt(getPlotType());
		out.writeObject(tableModel.getData());
		out.writeDouble(numClassesPPF.getCurrentValue());
		out.writeDouble(ageClassToViewPPF.getCurrentValue());
		out.writeDouble(runTimePPF.getCurrentValue());
		out.writeDouble(initialL1PPF.getCurrentValue());
		out.writeObject(sp.getMatrix());
		out.writeObject(sp.getPopulations());
		out.writeObject(sp.getTitles());
	}

	@Override
	public void readExternal(java.io.ObjectInput in) {
		try  {
			setPlotType(in.readInt());
			tableModel.setData(in.readObject());
			numClassesPPF.setCurrentValue(in.readDouble());
			ageClassToViewPPF.setCurrentValue(in.readDouble());
			runTimePPF.setCurrentValue(in.readDouble());
			initialL1PPF.setCurrentValue(in.readDouble());
			sp.setData((double[][]) in.readObject(), (double[]) in.readObject(), (String[]) in.readObject(), 0, true);
			lxmxTable.setModel(tableModel);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		sp.startTimer();
	}

}

class ASPGPanel_inputPane_changeAdapter implements javax.swing.event.ChangeListener {
	private ASPGPanel adaptee;

	ASPGPanel_inputPane_changeAdapter(ASPGPanel adaptee) {
		this.adaptee = adaptee;
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		adaptee.inputPane_stateChanged(e);
	}
}

