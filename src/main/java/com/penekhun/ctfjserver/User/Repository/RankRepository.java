package com.penekhun.ctfjserver.User.Repository;

import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import com.penekhun.ctfjserver.User.Dto.RankDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RankRepository{

    @PersistenceContext
    private EntityManager em;

    @Autowired
    ModelMapper modelMapper;

    public List<ProblemDto.Res.correctProblemList> findCorrectProblemList(){

        String sql = "select a " +
                "from Account m, AuthLog a " +
                "where a.accountIdx = m.id and a.isSuccess = true";
        List correctList = em.createQuery(sql).getResultList();

        List<ProblemDto.Res.correctProblemList> correctProblemLists = new ArrayList<>();
        correctList.forEach(s -> correctProblemLists.add(modelMapper.map(s, ProblemDto.Res.correctProblemList.class)));
        return correctProblemLists;
    }

    public List<RankDto.ProbListForDynamicScore> findProbSolver(){

        List<Object[]> resultList = em.createQuery("select a.problem.id, a.problem.maxScore, a.problem.minScore, a.problem.solveThreshold, count(a.problem) from AuthLog a where a.isSuccess = true group by a.problem").getResultList();
        //SELECT problem_idx, count(problem_idx) FROM ctf.AuthLog where is_success=true group By problem_idx
        List<Object[]> resultList2 = em.createQuery("select p.id, p.maxScore, p.minScore, p.solveThreshold from Problem p where p.id NOT IN (select a.problem from AuthLog a where a.isSuccess = true)").getResultList();
        //SELECT idx, max_score, min_score, solve_threshold FROM ctf.Problem where idx NOT IN (SELECT problem_idx FROM ctf.AuthLog WHERE is_success != true)

        List<RankDto.ProbListForDynamicScore> outputList = new ArrayList<>();

        if (resultList != null)
            for(Object[] row : resultList){
                RankDto.ProbListForDynamicScore item = RankDto.ProbListForDynamicScore.builder()
                        .id((Integer) row[0])
                        .maxScore((Integer) row[1])
                        .minScore((Integer) row[2])
                        .solveThreshold((Integer) row[3])
                        .solverCount((Long) row[4]).build();
                outputList.add(item);
            }

        if (resultList2 != null)
            for (Object[] row : resultList2) {
                RankDto.ProbListForDynamicScore item = RankDto.ProbListForDynamicScore.builder()
                        .id((Integer) row[0])
                        .maxScore((Integer) row[1])
                        .minScore((Integer) row[2])
                        .solveThreshold((Integer) row[3])
                        .solverCount(0L)
                        .calculatedScore((Integer) row[1]).build();
                outputList.add(item);
            }


        return outputList;
    }

    public List<RankDto> findWhoSolveProb(){
        List<Object[]> resultList = em.createQuery("SELECT a.accountIdx, a.problem.id FROM AuthLog a where a.isSuccess=true").getResultList();
//        for (Object[] objects : resultList) {
//
//        }
        return null;

    }

}
